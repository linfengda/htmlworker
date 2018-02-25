package com.itextpdf.tool.xml.css;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.FileRetrieveImpl;

public class StyleAttrCSSResolver implements CSSResolver {
	
	private CssInheritanceRules inheritRules;
	private CssFiles cssFiles;
	private CssUtils utils;
	private FileRetrieve fileRetrieve;
	
	
	public StyleAttrCSSResolver() {
		this(new DefaultCssInheritanceRules(), new CssFilesImpl(), CssUtils.getInstance(), new FileRetrieveImpl());
	}
	
	public StyleAttrCSSResolver(final CssFiles cssFiles) {
		this(new DefaultCssInheritanceRules(), cssFiles, CssUtils.getInstance(), new FileRetrieveImpl());
	}

	public StyleAttrCSSResolver(final CssInheritanceRules inheritRules, final CssFiles cssFiles, final CssUtils utils, final FileRetrieve fileRetrieve) {
		this.inheritRules = inheritRules;
		this.cssFiles = cssFiles;
		this.utils = utils;
		this.fileRetrieve = fileRetrieve;
	}

	@Override
	public void resolve(Tag t) {
		
		Map<String, String> css = t.getCSS();		//标签最终的CSS
		Map<String, String> tagCss = new LinkedHashMap<String, String>();	//从CSS样式表和标签style属性获取的CSS
		
		// 解析CSS文件
		if (null != cssFiles && cssFiles.hasFiles()) {
			tagCss = cssFiles.getCSS(t);
			if (t.getName().equalsIgnoreCase(HTML.Tag.P) || t.getName().equalsIgnoreCase(HTML.Tag.TD)) {
				
				Map<String, String> listCss = cssFiles.getCSS(new Tag(HTML.Tag.UL));
				if (listCss.containsKey(CSS.Property.LIST_STYLE_TYPE)) {	// list-style-type的样式
					css.put(CSS.Property.LIST_STYLE_TYPE, listCss.get(CSS.Property.LIST_STYLE_TYPE));
				}
			}
		}
		
		// 解析style属性
		Map<String, String> attributes = t.getAttributes();
		if (null != attributes && !attributes.isEmpty()) {
			if (attributes.get(HTML.Attribute.CELLPADDING) != null) {
				tagCss.putAll(utils.parseBoxValues(attributes.get(HTML.Attribute.CELLPADDING), "cellpadding-", ""));
			}
			if (attributes.get(HTML.Attribute.CELLSPACING) != null) {
				tagCss.putAll(utils.parseBoxValues(attributes.get(HTML.Attribute.CELLSPACING), "cellspacing-", ""));
			}
			
			String style = attributes.get(HTML.Attribute.STYLE);
			if (null != style && style.length() > 0) {
				Map<String, String> styleCss = new LinkedHashMap<String, String>();
				String[] styles = style.split(";");
				for (String s : styles) {
					String[] part = s.split(":", 2);
					if (part.length == 2) {
						String key = utils.stripDoubleSpacesTrimAndToLowerCase(part[0]);
						String value = utils.stripDoubleSpacesAndTrim(part[1]);
						parseAttributeValue(styleCss, key, value);
					}
				}
				tagCss.putAll(styleCss);
			}
		}
		
		// 特殊标签处理
        if (t.getName() != null) {
            if(t.getName().equalsIgnoreCase(HTML.Tag.I) || t.getName().equalsIgnoreCase(HTML.Tag.CITE)
                    || t.getName().equalsIgnoreCase(HTML.Tag.EM) || t.getName().equalsIgnoreCase(HTML.Tag.VAR)
                    || t.getName().equalsIgnoreCase(HTML.Tag.DFN) || t.getName().equalsIgnoreCase(HTML.Tag.ADDRESS)) {
                tagCss.put(CSS.Property.FONT_STYLE, CSS.Value.ITALIC);
            }
            else if (t.getName().equalsIgnoreCase(HTML.Tag.B) || t.getName().equalsIgnoreCase(HTML.Tag.STRONG)) {
                tagCss.put(CSS.Property.FONT_WEIGHT, CSS.Value.BOLD);
            }
            else if (t.getName().equalsIgnoreCase(HTML.Tag.U) || t.getName().equalsIgnoreCase(HTML.Tag.INS)) {
                tagCss.put(CSS.Property.TEXT_DECORATION, CSS.Value.UNDERLINE);
            }
            else if (t.getName().equalsIgnoreCase(HTML.Tag.S) || t.getName().equalsIgnoreCase(HTML.Tag.STRIKE)
                    || t.getName().equalsIgnoreCase(HTML.Tag.DEL)) {
                tagCss.put(CSS.Property.TEXT_DECORATION, CSS.Value.LINE_THROUGH);
            }
            else if (t.getName().equalsIgnoreCase(HTML.Tag.BIG)){
                tagCss.put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
            }
            else if (t.getName().equalsIgnoreCase(HTML.Tag.SMALL)){
                tagCss.put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
            }
            else if (t.getName().equals(HTML.Tag.FONT)) {
                String font_family = t.getAttributes().get(HTML.Attribute.FACE);
                if (font_family != null) css.put(CSS.Property.FONT_FAMILY, font_family);
                String color = t.getAttributes().get(HTML.Attribute.COLOR);
                if (color != null) css.put(CSS.Property.COLOR, color);
                String size = t.getAttributes().get(HTML.Attribute.SIZE);
                if (size != null) {
                    if(size.equals("1"))        css.put(CSS.Property.FONT_SIZE, CSS.Value.XX_SMALL);
                    else if(size.equals("2"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.X_SMALL);
                    else if(size.equals("3"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.SMALL);
                    else if(size.equals("4"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.MEDIUM);
                    else if(size.equals("5"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.LARGE);
                    else if(size.equals("6"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.X_LARGE);
                    else if(size.equals("7"))   css.put(CSS.Property.FONT_SIZE, CSS.Value.XX_LARGE);

                }
            }
            else if (t.getName().equals(HTML.Tag.A)) {
                css.put(CSS.Property.TEXT_DECORATION, CSS.Value.UNDERLINE);
                css.put(CSS.Property.COLOR, "blue");
            }
        }
		
		// 解析父类可继承属性
		if (null != t.getParent() && null != t.getParent().getCSS()) {
			Map<String, String> parentCss = t.getParent().getCSS();
			
			for (Entry<String, String> pc : parentCss.entrySet()) {
				String key = pc.getKey();
				String value = pc.getValue();
				if ((tagCss.containsKey(key) && CSS.Value.INHERIT.equalsIgnoreCase(tagCss.get(key))) || (!tagCss.containsKey(key) && canInherite(t, key))) {
					
					if (key.contains(CSS.Property.CELLPADDING) && (HTML.Tag.TD.equals(t.getName()) || HTML.Tag.TH.equals(t.getName()))) {
						String paddingKey = key.replace(CSS.Property.CELLPADDING, CSS.Property.PADDING);	// 将TD和TH元素cellpadding属性转为padding，PDF元素转换只支持padding属性？
						tagCss.put(paddingKey, value);
					}else{
						css.put(key, value);
					}
				}
			}
		}
		
        
		// 加到最终CSS，如果value!=inherit则覆盖
		for (Entry<String, String> kv : tagCss.entrySet()) {
			if (!kv.getValue().equalsIgnoreCase(CSS.Value.INHERIT)) {
				if (kv.getKey().equals(CSS.Property.TEXT_DECORATION)) {
					String oldValue = css.get(kv.getKey());
                    css.put(kv.getKey(), mergeTextDecorationRules(oldValue, kv.getValue()));
				}else{
					css.put(kv.getKey(), kv.getValue());
				}
			}
		}
        
	}

	
    private String mergeTextDecorationRules(String oldRule, String newRule) {
        if (CSS.Value.NONE.equals(newRule))
            return newRule;
        TreeSet<String> attrSet = new TreeSet<String>();
        if (oldRule != null)
            Collections.addAll(attrSet, oldRule.split("\\s+"));
        if (newRule != null)
            Collections.addAll(attrSet, newRule.split("\\s+"));
        StringBuilder resultantStr = new StringBuilder();
        for (String attr : attrSet) {
            if (attr.equals(CSS.Value.NONE) || attr.equals(CSS.Value.INHERIT))
                continue;
            if (resultantStr.length() > 0)
                resultantStr.append(' ');
            resultantStr.append(attr);
        }
        return resultantStr.length() == 0 ? null : resultantStr.toString();
    }

	/**
	 * 解析CSS属性值，以便后期PDF处理
	 * 例如：border-width: 20px 30px 10px 10px ---->  {border-top-width: 20,border-right-width: 30,border-bottom-width: 10,border-left-width: 10}
	 * 
	 * @param css the css map to populate
	 * @param key the property
	 * @param value the value
	 */
	private void parseAttributeValue(final Map<String, String> css, final String key, final String value) {
		if (CSS.Property.BORDER.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBorder(value, null));
		} else if (CSS.Property.BORDER_TOP.equalsIgnoreCase(key)) {
            css.putAll(utils.parseBorder(value, CSS.Property.BORDER_TOP));
        } else if (CSS.Property.BORDER_BOTTOM.equalsIgnoreCase(key)) {
            css.putAll(utils.parseBorder(value, CSS.Property.BORDER_BOTTOM));
        } else if (CSS.Property.BORDER_LEFT.equalsIgnoreCase(key)) {
            css.putAll(utils.parseBorder(value, CSS.Property.BORDER_LEFT));
        } else if (CSS.Property.BORDER_RIGHT.equalsIgnoreCase(key)) {
            css.putAll(utils.parseBorder(value, CSS.Property.BORDER_RIGHT));
        } else if (CSS.Property.BORDER_WIDTH.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBoxValues(value, "border-", "-width"));
		} else if (CSS.Property.BORDER_STYLE.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBoxValues(value, "border-", "-style"));
		} else if (CSS.Property.BORDER_COLOR.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBoxValues(value, "border-", "-color"));
		} else if (CSS.Property.MARGIN.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBoxValues(value, "margin-", ""));
		} else if (CSS.Property.PADDING.equalsIgnoreCase(key)) {
			css.putAll(utils.parseBoxValues(value, "padding-", ""));
		} else if (CSS.Property.FONT.equalsIgnoreCase(key)) {
			css.putAll(utils.processFont(value));
		} else if (CSS.Property.LIST_STYLE.equalsIgnoreCase(key)) {
			css.putAll(utils.processListStyle(value));
		} else if (key.toLowerCase().contains(CSS.Property.BACKGROUND)) {
            Map<String, String> backgroundStyles = utils.processBackground(value);
            for (String backgroundKey : backgroundStyles.keySet()) {
                if (!css.containsKey(backgroundKey)) {
                    css.put(backgroundKey, backgroundStyles.get(backgroundKey));
                }
            }
        } else {
			css.put(key, value);
		}
	}
	
	private boolean canInherite(final Tag t, final String property) {
		if (null != this.inheritRules) {
			return this.inheritRules.isAutoInheritCss(t, property);
		}
		return true;
	}

	@Override
	public void addCss(String content, String charSet, boolean isPersistent) throws CssResolverException {
		CssFileProcessor proc = new CssFileProcessor();
		try {
			fileRetrieve.processFromStream(new ByteArrayInputStream(content.getBytes(charSet)), proc);
			CssFile css = proc.getCss();
			css.isPersistent(isPersistent);
			this.cssFiles.add(css);
		} catch (UnsupportedEncodingException e) {
			throw new CssResolverException(e);
		} catch (IOException e) {
			throw new CssResolverException(e);
		}
	}

	@Override
	public void addCssFile(String href, boolean isPersistent) throws CssResolverException {
		CssFileProcessor cssFileProcessor = new CssFileProcessor();
		try {
			fileRetrieve.processFromHref(href, cssFileProcessor);
		} catch (IOException e) {
			throw new CssResolverException(e);
		}
		CssFile css = cssFileProcessor.getCss();
		css.isPersistent(isPersistent);
		this.cssFiles.add(css);
	}

	@Override
	public void addCss(String content, boolean isPersistent) throws CssResolverException {
		CssFileProcessor proc = new CssFileProcessor();
		FileRetrieve retrieve = new FileRetrieveImpl();
		try {
			retrieve.processFromStream(new ByteArrayInputStream(content.getBytes()), proc);
			CssFile css = proc.getCss();
			css.isPersistent(isPersistent);
			this.cssFiles.add(css);
		} catch (UnsupportedEncodingException e) {
			throw new CssResolverException(e);
		} catch (IOException e) {
			throw new CssResolverException(e);
		}
		
	}

	@Override
	public void addCss(CssFile file) {
		this.cssFiles.add(file);
	}

	@Override
	public CSSResolver clear() throws CssResolverException {
		cssFiles.clear();
		return this;
	}

	
	
	

}

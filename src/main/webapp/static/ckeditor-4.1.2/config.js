/**
 * @license Copyright (c) 2003-2013, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.height = 480;
	//所有的内容都不转义
	config.entities = false;
	 //是否使用完整的html编辑模式 如使用，其源码将包含：<html><body></body></html>等标签
    config.fullPage = true; 
    config.language = 'zh-cn'; //配置语言  
    config.font_names='宋体/宋体;黑体/黑体;仿宋/仿宋_GB2312;华文中宋/华文中宋;楷体/楷体_GB2312;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;' + config.font_names;
    config.toolbar = "Full";  
	 //编辑器中回车产生的标签
    config.enterMode = CKEDITOR.ENTER_BR; //可选：CKEDITOR.ENTER_BR或CKEDITOR.ENTER_DIV 
    //config.RemoveAttributes = 'class,style,lang,width,height,align,hspace,valign' ;
    //config.RemoveFormatTags = 'b,big,code,del,dfn,em,font,i,ins,kbd,q,samp,small,span,strike,strong,sub,sup,tt,u,var' ;
//    config.protectedSource.push( /<\s*iframe[\s\S]*?>/gi ) ; // <iframe> tags.               //一下是后天验证非法数据
//    config.protectedSource.push( /<\s*frameset[\s\S]*?>/gi ) ; // <frameset> tags.
//    config.protectedSource.push( /<\s*frame[\s\S]*?>/gi ) ; // <frame> tags.
    config.protectedSource.push( /<\s*script[\s\S]*?\/script\s*>/gi ) ; // <SCRIPT> tags.
//    config.protectedSource.push( /<%[\s\S]*?%>/g ) ; // ASP style server side code
//    config.protectedSource.push( /<\?[\s\S]*?\?>/g ) ; // PHP style server side code
//    config.protectedSource.push( /(<asp:[^\>]+>[\s|\S]*?<\/asp:[^\>]+>)|(<asp:[^\>]+\/>)/gi ) ;
};

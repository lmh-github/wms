package com.gionee.wms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 菜单类: 既表示一个菜单的节点,又表示一颗菜单树
 * 
 * @author kevin
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer sysId;
	private String sysName;
	private Long parent_id;
	private String parent_name;
	private Long id;
	private String name;
	private String url;
	private Menu parentMenu;
	private String create_time;
	private long creator;
	private String modify_time;
	private long modifier;
	private List<Menu> childMenus = new ArrayList<Menu>();
	private String path;
	private List<Menu> child;
	
	
	public List<Menu> getChild() {
		return child;
	}

	public void setChild(List<Menu> child) {
		this.child = child;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	// 根节点id
	public final static Long ROOT_ID = 0L;
	// 跟节点name
	public final static String ROOT_NAME = "ROOT";

	// 是否叶子节点
	public boolean isLeaf() {
		return this.childMenus == null || this.childMenus.isEmpty();
	}

	public Menu() {
		super();
	}

	public Menu(Long id) {
		this();
		this.id = id;
	}

	public Menu(Long id, String name) {
		this(id);
		this.name = name;
	}

	public Menu(Long id, String name, String url) {
		this(id, name);
		this.url = url;
	}

	public Integer getSysId() {
		return sysId;
	}

	public void setSysId(Integer sysId) {
		this.sysId = sysId;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parentName) {
		parent_name = parentName;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public long getCreator() {
		return creator;
	}

	public void setCreator(long creator) {
		this.creator = creator;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modifyTime) {
		modify_time = modifyTime;
	}

	public long getModifier() {
		return modifier;
	}

	public void setModifier(long modifier) {
		this.modifier = modifier;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parentId) {
		parent_id = parentId;
	}

	public void setChildMenus(List<Menu> childMenus) {
		this.childMenus = childMenus;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return 所有的子菜单
	 */
	public List<Menu> getChildMenus() {
		return childMenus;
	}

	/**
	 * 增加一个子菜单
	 * 
	 * @param childMenu
	 *            子菜单
	 */
	public void addChildMenu(Menu childMenu) {
		this.childMenus.add(childMenu);
	}

	/**
	 * 增加一些子菜单
	 * 
	 * @param childMenus
	 *            子菜单集合
	 */
	public void addChildMenus(List<Menu> childMenus) {
		this.childMenus.addAll(childMenus);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}

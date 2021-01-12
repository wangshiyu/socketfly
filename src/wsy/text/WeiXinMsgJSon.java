package wsy.text;

import java.util.HashMap;

/** 
* @author black
* @version 创建时间：2016年8月31日 上午10:55:32 
* 微信数据实体
*/
public class WeiXinMsgJSon {
	/**id 和业务无关*/
	private String id;
	// 登录用户名
	private String loginUserName;
	// 登录的微信用户名
	private String loginWeiXinUserName;
	 //是否属于朋友圈消息
	public Boolean isSnsInfo;
	// 是否属于群消息
	private Boolean isGroupMsg;
	// 微信群id
	private String chatroomname;
	// 微信群昵称
	private String chatroomnickname;
	// 微信群好友id列表
	private String memberlist;
	// 微信群好友昵称列表
	private String displayname;
	// 微信群创建者id
	private String roomowner;

	// 发送消息的id
	private String talkerId;
	// 消息发送者昵称
	private String talkerName;
	// 是否属于主动发送的消息
	private Boolean isSend;
	// 消息发送时间
	private long createTime;
	// 发送消息文本消息
	private String content;
	// 音视频、图片消息id
	private String imgPath;
	
	public WeiXinMsgJSon()
	{
		loginUserName="";
		loginWeiXinUserName="";
		isSnsInfo=false;
		isGroupMsg=false;
		chatroomname="";
		chatroomnickname="";
		memberlist="";
		displayname="";
		roomowner="";
		talkerId="";
		talkerName="";
		isSend=false;
		createTime=0;
		content="";
		imgPath="";
	}
	public HashMap<String,Object> toHashMap()
	{
		HashMap<String,Object> retMap=new HashMap<String, Object>();
		retMap.put("loginUserName", loginUserName);
		retMap.put("loginWeiXinUserName",loginWeiXinUserName);
		retMap.put("isSnsInfo", isSnsInfo);
		retMap.put("isGroupMsg", isGroupMsg);
		retMap.put("chatroomname",chatroomname);
		retMap.put("chatroomnickname", chatroomnickname);
		retMap.put("memberlist",memberlist);
		retMap.put("displayname",displayname);
		retMap.put("roomowner", roomowner);
		retMap.put("talkerId", talkerId);
		retMap.put("talkerName", talkerName);
		retMap.put("isSend", isSend);
		retMap.put("createTime", createTime);
		retMap.put("content", content);
		retMap.put("imgPath", imgPath);
		return (HashMap<String, Object>) retMap;
	}
	
	@Override
	public String toString() {
		return "WeiXinMsgJSon [id=" + id + ", loginUserName=" + loginUserName + ", loginWeiXinUserName="
				+ loginWeiXinUserName + ", isSnsInfo=" + isSnsInfo + ", isGroupMsg=" + isGroupMsg + ", chatroomname="
				+ chatroomname + ", chatroomnickname=" + chatroomnickname + ", memberlist=" + memberlist
				+ ", displayname=" + displayname + ", roomowner=" + roomowner + ", talkerId=" + talkerId
				+ ", talkerName=" + talkerName + ", isSend=" + isSend + ", createTime=" + createTime + ", content="
				+ content + ", imgPath=" + imgPath + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginUserName() {
		return loginUserName;
	}
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	public String getLoginWeiXinUserName() {
		return loginWeiXinUserName;
	}
	public void setLoginWeiXinUserName(String loginWeiXinUserName) {
		this.loginWeiXinUserName = loginWeiXinUserName;
	}
	public Boolean getIsSnsInfo() {
		return isSnsInfo;
	}
	public void setIsSnsInfo(Boolean isSnsInfo) {
		this.isSnsInfo = isSnsInfo;
	}
	public Boolean getIsGroupMsg() {
		return isGroupMsg;
	}
	public void setIsGroupMsg(Boolean isGroupMsg) {
		this.isGroupMsg = isGroupMsg;
	}
	public String getChatroomname() {
		return chatroomname;
	}
	public void setChatroomname(String chatroomname) {
		this.chatroomname = chatroomname;
	}
	public String getChatroomnickname() {
		return chatroomnickname;
	}
	public void setChatroomnickname(String chatroomnickname) {
		this.chatroomnickname = chatroomnickname;
	}
	public String getMemberlist() {
		return memberlist;
	}
	public void setMemberlist(String memberlist) {
		this.memberlist = memberlist;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getRoomowner() {
		return roomowner;
	}
	public void setRoomowner(String roomowner) {
		this.roomowner = roomowner;
	}
	public String getTalkerId() {
		return talkerId;
	}
	public void setTalkerId(String talkerId) {
		this.talkerId = talkerId;
	}
	public String getTalkerName() {
		return talkerName;
	}
	public void setTalkerName(String talkerName) {
		this.talkerName = talkerName;
	}
	public Boolean getIsSend() {
		return isSend;
	}
	public void setIsSend(Boolean isSend) {
		this.isSend = isSend;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	
	
}

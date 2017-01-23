package dtos;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Data Transfer Object de la tabla Audience. Cada atributo representa una columna de
 * la tabla AUDIENCE.
 * @author mmaccio
 *
 */
public class Audience_DTO {
	
	private int socialNetworkId;
	private String userId;
	private int genderId;
	private Date bithDate;
	private String displayName;
	private String preferredUserName;
	private int statusesCount;
	private int friendsCount;
	private int followersCount;
	private byte isParent;
	private byte isVerified;
	private String location;
	private Timestamp ts;
	

	public Audience_DTO(String userId) {
		super();
		this.userId = userId;
	}

	public Audience_DTO(int socialNetworkId, String userId, int genderId, Date bithDate, String displayName,
			String preferredUserName, int statusesCount, int friendsCount, int followersCount, byte isParent,
			byte isVerified) {
		super();
		this.socialNetworkId = socialNetworkId;
		this.userId = userId;
		this.genderId = genderId;
		this.bithDate = bithDate;
		this.displayName = displayName;
		this.preferredUserName = preferredUserName;
		this.statusesCount = statusesCount;
		this.friendsCount = friendsCount;
		this.followersCount = followersCount;
		this.isParent = isParent;
		this.isVerified = isVerified;
	}
	
	public int getSocialNetworkId() {
		return socialNetworkId;
	}
	public void setSocialNetworkId(int socialNetworkId) {
		this.socialNetworkId = socialNetworkId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public Date getBithDate() {
		return bithDate;
	}
	public void setBithDate(Date bithDate) {
		this.bithDate = bithDate;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPreferredUserName() {
		return preferredUserName;
	}
	public void setPreferredUserName(String preferredUserName) {
		this.preferredUserName = preferredUserName;
	}
	public int getStatusesCount() {
		return statusesCount;
	}
	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}
	public int getFriendsCount() {
		return friendsCount;
	}
	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}
	public int getFollowersCount() {
		return followersCount;
	}
	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}
	public byte getIsParent() {
		return isParent;
	}
	public void setIsParent(byte isParent) {
		this.isParent = isParent;
	}
	public byte getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(byte isVerified) {
		this.isVerified = isVerified;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Timestamp getTimeStamp() {
		return ts;
	}

	public void setTimeStamp(Timestamp ts) {
		this.ts = ts;
	}
	
	
}

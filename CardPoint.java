public class CardPoint {

	private int point;
	private String type;
	private String realName;

	public CardPoint(int p, String t, String rname) {
		this.point = p;
		this.type = t;
		this.realName = rname;
	}

	public int getPoint() {
		return point;
	}

	public String getType() {
		return type;
	}

	public String getRealName() {
		return realName;
	}
}
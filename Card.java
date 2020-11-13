import java.io.Serializable;

public class Card implements Serializable {
   private String value;
   private String type;
   private int points;

   public Card(String value, String type, int points) {
      this.value = value;
      this.type = type;
      this.points = points;
   }

   public String getType() {
      return type;
   }

   public String getValue() {
      return value;
   }

   public void setPoint(int points) {
      this.points = points;
   }

   public int getPoints() {
      return points;
   }

   public void setCardReset() {
      this.value = "0";
      this.type = "N";
      this.points = 0;
   }
}
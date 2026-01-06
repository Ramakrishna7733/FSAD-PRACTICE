package package1;

public class Rectangle extends TwoDShape;
//members
private double s2;

public void setS2(double s2)  {
  this.s2 = (s2>0 && s2<=200) ? s2: 0;

}

private Rectangle()  {
  super(0);
  this.s2=0;
}

public Rectangle(double s1,double s2) {
  this();
  this.setS1(s1);
  this.setS2(s2);
}
public double calArea()  {
  return this s1*this.s2;
}


public double calPeri()  {
  return 2*(this.s1+this.s2);
}
}
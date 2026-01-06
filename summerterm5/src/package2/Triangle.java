package package2;

public class Triangle extends TwoDShape{
	private double s2,s3;
	public void setS2S3(double s2 , double s3)
	{
		if(s2>0 && s3>0)
		{
		if(s2+s3<=this.s1 || s2+this.s1<=s3 || s3+this.s1<=s2)
		{
			this.s2=this.s3=0;
		}
		else
		{
			 this.s2=this.s3=0;
		}
		private Triangle()
		{
			super(0);
		}
		public Triangle(double s1,double s2,double s3)
		{
			super(s1);
			this .setS2S3(s2,s3);
		}
		public double calcPeri()
		{
			return S1+S2+S3;
		}
		public double calcArea()
		{
			double sp= this.calcPeri()/2;
			return Math.sqrt()
		}
		}
	}

}

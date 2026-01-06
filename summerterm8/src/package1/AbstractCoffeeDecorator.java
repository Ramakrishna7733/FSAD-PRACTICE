package package1;

public abstract class AbstractCoffeeDecorator implements Icoffee {
	protected ICoffee c;
	public AbstractCoffeeDecorator(ICoffee c)
	{
		this.c=C;
	}
	public String getDecription() {
		return this.c.getDescription();
	}
public double getAmount()
{
	return this.c.getAmount();
}
}

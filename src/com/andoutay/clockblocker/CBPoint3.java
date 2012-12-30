package com.andoutay.clockblocker;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.block.Block;

public class CBPoint3
{
	public int x;
	public int y;
	public int z;
	
	CBPoint3(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	CBPoint3(Block b)
	{
		x = b.getX();
		y = b.getY();
		z = b.getZ();
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ", " + z + ")"; 
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;
		
		CBPoint3 p = (CBPoint3) obj;
		return x == p.x && y == p.y && z == p.z;
	}
	
	public int hashCode()
	{
		return new HashCodeBuilder(1619, 191).append(x).append(y).append(z).toHashCode();
	}
}

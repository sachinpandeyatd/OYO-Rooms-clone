package com.oyo_rooms_with_hib;

import java.sql.Date;

public class Test {
	public static void main(String[] args) {
		OyoDAO obj = new OyoDAO();
		Booking booking = obj.bookings("R003");
		OyoDAO obj1 = new OyoDAO();
		
		int noOfDays = obj1.noOfDays(booking.getChkInDate(), booking.getChkOutDate());
		
		System.out.println(noOfDays);
	}
}

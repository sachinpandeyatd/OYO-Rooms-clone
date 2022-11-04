package com.oyo_rooms_with_hib;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class OyoDAO {
	SessionFactory sessionFactory;
	
	private String roomId() {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Rooms.class);
		List<Rooms> roomList = criteria.list();
		String roomId = roomList.get(roomList.size() - 1).getRoomId();
		
		roomId = String.format("%03d", Integer.parseInt(roomId.substring(1))+1);
		roomId = "R"+ roomId;
		
		return roomId;
	}
	
	public String addRoomDAO(Rooms rooms) {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		String roomId = roomId();
		rooms.setRoomId(roomId);
		rooms.setStatus(Status.AVAILABLE);
		
		Transaction transaction = session.beginTransaction();
		session.save(rooms);
		transaction.commit();
		session.close();
		
		return "Room Added.";
	}
	
	public List<Rooms> showAvailRooms() {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Rooms.class);
		criteria.add(Restrictions.eq("status", Status.AVAILABLE));
		List<Rooms> roomList = criteria.list();
		
		return roomList;
	}
	
	public Rooms room(String roomId) {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Rooms.class);
		criteria.add(Restrictions.eq("roomId", roomId));
		List<Rooms> roomList = criteria.list();
		
		return roomList.get(0);
	}
	
	public Booking bookings(String roomId) {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Booking.class);
		criteria.add(Restrictions.eq("roomId", roomId));
		List<Booking> bookingList = criteria.list();
		
		return bookingList.get(bookingList.size() - 1);
	}
	
	public Date convertDate(java.util.Date dt) {
		java.sql.Date sqlDate = new java.sql.Date(dt.getTime());
		return sqlDate;
	}
	
	public String bookRoomDAO(Booking booking) {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		java.util.Date date = new java.util.Date();
		java.sql.Date bookDate = new Date(date.getTime());
		booking.setBookDate(bookDate);
		
		Transaction transaction = session.beginTransaction();
		session.save(booking);
		transaction.commit();
		session.close();
		
		Rooms rooms = room(booking.getRoomId());
		rooms.setStatus(Status.BOOKED);
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(rooms);
		transaction.commit();
		session.close();
		
		return "Room Booked.";
	}
	
	public int noOfDays(Date chkInDate, Date chkOutDate) {
		
		int totalDays= chkOutDate.getDate() - chkInDate.getDate();
		return ++totalDays;
	}
	
	public String checkoutDAO(String roomId) {
		sessionFactory = SessionHelper.getConnection();
		Session session = sessionFactory.openSession();
		
		Checkout checkout = new Checkout();
		Booking booking = bookings(roomId);
		Rooms rooms = room(roomId);
		
		int noOfDays = booking.getChkOutDate().getDate() - booking.getChkInDate().getDate() + 1;
		long billAmt = noOfDays * rooms.getCostPerDay();
		
		checkout.setRoomId(roomId);
		checkout.setNoOfDays(noOfDays);
		checkout.setBillAmt(billAmt);
		checkout.setBookId(booking.getBookId().intValue());
		
		Transaction transaction = session.beginTransaction();
		session.save(checkout);
		transaction.commit();
		session.close();
		
		rooms.setStatus(Status.AVAILABLE);
		
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		session.update(rooms);
		transaction.commit();
		session.close();
		
		return "Room With ID " + roomId + " and bill amount of " + billAmt + " for " + noOfDays + " days checked out successfully.";
	}
}

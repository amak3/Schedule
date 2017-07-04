package application;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class Row
{
	private int id;
	private String when; 
	private String group;
	private String subject;
	private int year;
	private String study;
	private String leader;
	private String room1;
	private String room2;

	public int getId()
	{
		return id;
	}

	public String getGroup()
	{
		return group;
	}

	public String getSubject()
	{
		return subject;
	}

	public int getYear()
	{
		return year;
	}

	public String getStudy()
	{
		return study;
	}

	public String getLeader()
	{
		return leader;
	}

	public String getRoom1()
	{
		return room1;
	}

	public String getRoom2()
	{
		return room2;
	}

	public LocalDate getWhen()
	{
		Instant instant = Instant.parse(this.when);
		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
				
		LocalDate when = ldt.toLocalDate();

		return when;
	}
	
	public LocalTime getHour()
	{
		Instant instant = Instant.parse(this.when);
		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		LocalTime hour = ldt.toLocalTime();
		
		return hour;
	}

	@Override
	public String toString()
	{
		return "Row [id=" + id + ", when=" + when + ", group=" + group + ", subject=" + subject + ", year=" + year
				+ ", study=" + study + ", leader=" + leader + ", room1=" + room1 + ", room2=" + room2 + "]";
	}
}

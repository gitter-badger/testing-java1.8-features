package com.cristian.mylab;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalDouble;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext-service-test.xml")
public class FlightServiceImplTest {

	@Autowired
	private FlightService flightService;

	private Collection<Flight> getFlightCollection(LocalDate date,
			boolean fullFlight) {

		Collection<Flight> flightCollection = new ArrayList<Flight>();

		Flight flight = new Flight();
		flight.setCode("1");
		flight.setDate(date);
		flight.setDestination("London");
		Duration duration = new Duration();
		duration.setHours(1);
		duration.setMinutes(30);
		flight.setDuration(duration);
		flight.setNumPassengers(180);
		flight.setNumSeats(200);
		flight.setPrice(new Double(1000));
		flightCollection.add(flight);

		flight = new Flight();
		flight.setCode("2");
		flight.setDate(date);
		flight.setDestination("Madrid");
		duration = new Duration();
		duration.setHours(2);
		duration.setMinutes(30);
		flight.setDuration(duration);
		flight.setNumPassengers(180);
		flight.setNumSeats(255);
		flight.setPrice(new Double(800));
		flightCollection.add(flight);

		flight = new Flight();
		flight.setCode("3");
		flight.setDate(date);
		flight.setDestination("Paris");
		duration = new Duration();
		duration.setHours(3);
		duration.setMinutes(30);
		flight.setDuration(duration);
		flight.setNumPassengers(199);
		flight.setNumSeats(200);
		flight.setPrice(new Double(400));
		flightCollection.add(flight);

		flight = new Flight();
		flight.setCode("4");
		flight.setDate(date);
		flight.setDestination("Japan");
		duration = new Duration();
		duration.setHours(3);
		duration.setMinutes(30);
		flight.setDuration(duration);
		flight.setNumPassengers(180);
		flight.setNumSeats(200);
		if (fullFlight) {
			flight.setNumSeats(180);
		}
		flight.setPrice(new Double(2000));
		flightCollection.add(flight);
		return flightCollection;
	}

	@Test
	public void numberFlightByDayOk() {

		Assert.assertNotNull(flightService);
		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		long numberFlightByDay = flightService.numberFlightByDay(date,
				flightCollection);
		Assert.assertEquals(4, numberFlightByDay);
	}

	@Test
	public void numberFlightByDayOkWithZeroElements() {

		Assert.assertNotNull(flightService);
		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		date = LocalDate.of(2016, 5, 15);
		long numberFlightByDay = flightService.numberFlightByDay(date,
				flightCollection);
		Assert.assertEquals(0, numberFlightByDay);

	}

	@Test
	public void numberFullFlighZero() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		long numberFullFlight = flightService
				.numberFullFlight(flightCollection);
		Assert.assertEquals(0, numberFullFlight);

	}

	@Test
	public void numberFullFligh() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, true);

		long numberFullFlight = flightService
				.numberFullFlight(flightCollection);
		Assert.assertEquals(1, numberFullFlight);

	}
	
	@Test
	public void numberFlightByDayOkAndFull() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, true);
		long numberFlightByDayAndFull = flightService.numberFullFlightAndDate(flightCollection, date);
		Assert.assertEquals(1, numberFlightByDayAndFull);
	}
	
	
	@Test
	public void fullFlightAndDateAllMatchFalse() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, true);
		boolean allMatch = flightService.fullFlightAndDateAllMatch(flightCollection, date);
		Assert.assertFalse(allMatch);
	}
	
	
	@Test
	public void fullFlightAndDateAnyMatchTrue() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, true);
		boolean anyMatch = flightService.fullFlightAndDateAnyMatch(flightCollection, date);
		Assert.assertTrue(anyMatch);
	}
	
	@Test
	public void fullFlightAndDateAnyMatchFalse() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		boolean anyMatch = flightService.fullFlightAndDateAnyMatch(flightCollection, date);
		Assert.assertFalse(anyMatch);
	}
	
	@Test
	public void flightDateMinPrice() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		Optional<Flight> flightDateMinPrice = flightService.flightDateMinPrice(flightCollection, date);
		Assert.assertTrue(flightDateMinPrice.isPresent());
		Assert.assertTrue(flightDateMinPrice.get().getDestination().equals("Paris"));
		
	}
	
	@Test
	public void flightDateMinPriceEmptyFlightList() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = new ArrayList<Flight>();
		Optional<Flight> flightDateMinPrice = flightService.flightDateMinPrice(flightCollection, date);
		Assert.assertFalse(flightDateMinPrice.isPresent());
		
	}
	
	@Test
	public void flightDateMaxPrice() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		Optional<Flight> flightDateMaxPrice = flightService.flightDateMaxPrice(flightCollection, date);
		Assert.assertTrue(flightDateMaxPrice.isPresent());
		Assert.assertTrue(flightDateMaxPrice.get().getDestination().equals("Japan"));
		
	}
	
	@Test
	public void flightDateMaxPriceEmptyFlightList() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = new ArrayList<Flight>();
		//testing new optional object, null pointer has death
		Optional<Flight> flightDateMaxPrice = flightService.flightDateMaxPrice(flightCollection, date);
		Assert.assertFalse(flightDateMaxPrice.isPresent());
		
	}
	
	
	
	@Test
	public void flightDateMaxNumPassengers() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		Optional<Flight> flightDateMaxPrice = flightService.flightDateMaxNumPassengers(flightCollection, date);
		Assert.assertTrue(flightDateMaxPrice.isPresent());
		Assert.assertTrue(flightDateMaxPrice.get().getDestination().equals("Paris"));
		
	}
	
	@Test
	public void flightDateMaxNumSeats() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		Optional<Flight> flightDateMaxPrice = flightService.flightDateMaxNumSeats(flightCollection, date);
		Assert.assertTrue(flightDateMaxPrice.isPresent());
		Assert.assertTrue(flightDateMaxPrice.get().getDestination().equals("Madrid"));
		
	}
	
	
	@Test
	public void flightDateNumPassengersSum() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		int flightDateNumPassengersSum = flightService.flightDateNumPassengersSum(flightCollection, date);
		Assert.assertEquals(flightDateNumPassengersSum, 739);
		
	}
	
	@Test
	public void flightDatePriceAverage() {

		LocalDate date = LocalDate.of(2014, 5, 15);
		Collection<Flight> flightCollection = getFlightCollection(date, false);
		OptionalDouble flightDatePriceAverage = flightService.flightDatePriceAverage(flightCollection, date);
		Assert.assertTrue(flightDatePriceAverage.isPresent());
		Assert.assertTrue(flightDatePriceAverage.getAsDouble()==1050);
		
	}
	
	
	
	
	
	
	
	
	
}

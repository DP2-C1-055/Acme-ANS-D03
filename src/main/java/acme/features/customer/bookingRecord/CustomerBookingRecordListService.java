
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.BookingRecord.BookingRecord;
import acme.entities.booking.Booking;
import acme.features.customer.booking.CustomerBookingRepository;
import acme.realms.Customer.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository	repository;

	@Autowired
	private CustomerBookingRepository		customerBookingRepository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.customerBookingRepository.findBookingById(bookingId);
		status = super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<BookingRecord> bookingRecord;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("bookingId", int.class);
		super.getResponse().addGlobal("id", bookingId);
		booking = this.customerBookingRepository.findBookingById(bookingId);
		super.getResponse().addGlobal("draftModeBooking", booking.getDraftMode());
		bookingRecord = this.repository.findBookingRecordByBooking(bookingId);
		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		dataset.put("passengerName", bookingRecord.getPassenger().getFullName());
		dataset.put("bookingLocator", bookingRecord.getBooking().getLocatorCode());

		super.getResponse().addData(dataset);

	}

}

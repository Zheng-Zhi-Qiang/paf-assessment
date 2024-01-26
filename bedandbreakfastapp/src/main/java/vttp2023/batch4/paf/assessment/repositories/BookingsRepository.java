package vttp2023.batch4.paf.assessment.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch4.paf.assessment.errors.SQLInsertionError;
import vttp2023.batch4.paf.assessment.models.Bookings;
import vttp2023.batch4.paf.assessment.models.User;

import static vttp2023.batch4.paf.assessment.repositories.Queries.*;

@Repository
public class BookingsRepository {
	
	// You may add additional dependency injections

	// NOTE: Changed the query from % to ?
	public static final String SQL_SELECT_USER_BY_EMAIL = "select * from users where email like ?";

	@Autowired
	private JdbcTemplate template;

	// You may use this method in your task
	// NOTE: Changed the method to ensure search is possible
	public Optional<User> userExists(String email) {
		SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_EMAIL, "%" + email + "%");
		if (!rs.next())
			return Optional.empty();

		return Optional.of(new User(rs.getString("email"), rs.getString("name")));
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newUser(User user) throws SQLInsertionError {
		int result = template.update(SQL_INSERT_USER, user.email(), user.name());
		if (result < 1){
			System.out.println("failed to create user");
			throw new SQLInsertionError("User creation failed.");
		}
		
	}

	// TODO: Task 6
	// IMPORTANT: DO NOT MODIFY THE SIGNATURE OF THIS METHOD.
	// You may only add throw exceptions to this method
	public void newBookings(Bookings bookings) throws SQLInsertionError {
		int result = template.update(SQL_INSERT_BOOKING, bookings.getBookingId(), bookings.getListingId(), bookings.getDuration(), bookings.getEmail());
		if (result < 1){
			throw new SQLInsertionError("Booking creation failed.");
		}
	}
}

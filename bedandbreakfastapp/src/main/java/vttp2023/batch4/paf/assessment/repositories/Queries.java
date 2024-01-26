package vttp2023.batch4.paf.assessment.repositories;

public class Queries {
    public static final String SQL_INSERT_USER = """
            INSERT INTO users(email, name)
            VALUE(?, ?);
            """;
    public static final String SQL_INSERT_BOOKING = """
            INSERT INTO bookings(booking_id, listing_id, duration, email)
            VALUE(?, ?, ?, ?);
            """;
}

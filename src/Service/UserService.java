/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.UserDAO;
import DAO.UserDAO.UserAccount;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.regex.Pattern;
import Models.AuthUser;
import Models.RegistrationData;
import utils.PasswordUtils;

/**
 *
 * @author kiboi Validate registration values. Prevent public registration as an
 * administrator. Hash passwords before inserting users. Convert the form's role
 * names to the database values. Check passwords during login.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    private static final Pattern EMAIL_PATTERN
            = Pattern.compile(
                    "^[A-Za-z0-9._%+-]+@usiu\\.ac\\.ke$",
                    Pattern.CASE_INSENSITIVE
            );

    public void register(
            RegistrationData data,
            char[] password)
            throws SQLException, GeneralSecurityException {

        if (data == null || password == null) {
            throw new IllegalArgumentException(
                    "Registration details are required."
            );
        }

        String name = clean(data.fullName());
        String usiuId = clean(data.usiuID());
        String email = clean(data.email())
                .toLowerCase(Locale.ROOT);
        String phone = clean(data.phone());
        String role = clean(data.role())
                .toUpperCase(Locale.ROOT);

        if (name.isBlank() || usiuId.isBlank()) {
            throw new IllegalArgumentException(
                    "Name and USIU ID are required."
            );
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    "Enter a valid USIU email address."
            );
        }

        if (phone.isBlank()) {
            throw new IllegalArgumentException(
                    "Phone number is required."
            );
        }

        if (!role.equals("STUDENT")
                && !role.equals("STAFF")) {
            throw new IllegalArgumentException(
                    "Only Student and Staff registration is allowed."
            );
        }

        String programme = null;
        Integer year = null;
        String department = null;

        if (role.equals("STUDENT")) {

            programme = clean(data.programme());
            year = data.yearOfStudy();

            if (programme.isBlank()
                    || year == null
                    || year < 1
                    || year > 10) {
                throw new IllegalArgumentException(
                        "Enter a programme and valid year."
                );
            }

        } else {

            department = clean(data.department());

            if (department.isBlank()) {
                throw new IllegalArgumentException(
                        "Department is required."
                );
            }
        }

        if (password.length < 8 || password.length > 128) {
            throw new IllegalArgumentException(
                    "Password must be 8 to 128 characters."
            );
        }

        RegistrationData validated
                = new RegistrationData(
                        name,
                        usiuId,
                        email,
                        phone,
                        role,
                        programme,
                        year,
                        department
                );

        String hash = PasswordUtils.hashPassword(
                password
        );

        try {
            userDAO.createUser(validated, hash);
        } catch (SQLException ex) {

            if (ex instanceof java.sql.SQLIntegrityConstraintViolationException
                    || "23000".equals(ex.getSQLState())) {

                throw new IllegalArgumentException(
                        "This USIU ID or email is already registered."
                );
            }

            throw ex;
        }
    }

    public AuthUser login(
            String email,
            char[] password)
            throws SQLException, GeneralSecurityException {

        if (email == null || password == null
                || email.isBlank()
                || password.length == 0) {
            throw new IllegalArgumentException(
                    "Email and password are required."
            );
        }

        String normalizedEmail
                = email.trim().toLowerCase(Locale.ROOT);

        UserAccount account
                = userDAO.findByEmail(normalizedEmail);

        if (account == null) {
            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        boolean valid = PasswordUtils.verifyPassword(
                password,
                account.passwordHash()
        );

        if (!valid) {
            throw new IllegalArgumentException(
                    "Invalid email or password."
            );
        }

        if (!account.active()) {
            throw new IllegalArgumentException(
                    "This account has been deactivated."
            );
        }

        return new AuthUser(
                account.userId(),
                account.fullName(),
                account.role()
        );
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

}

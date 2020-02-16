package open.HL7PET.service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class SessionToken {
    private String token ;
    private Date lastUpdated;

    public SessionToken() {
        this.token = UUID.randomUUID().toString();
        this.lastUpdated = new Date();
    }

    public SessionToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    //Allows to compare just the token with a single String.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o != null && getClass() == o.getClass()) {
            SessionToken that = (SessionToken) o;
            return token.equals(that.token);
        } else if (o.getClass() == String.class) {
            return token.equals(o);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}

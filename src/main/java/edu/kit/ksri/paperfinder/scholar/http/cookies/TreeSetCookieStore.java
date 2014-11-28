package edu.kit.ksri.paperfinder.scholar.http.cookies;

import org.apache.http.annotation.GuardedBy;
import org.apache.http.cookie.Cookie;

import java.util.*;

/**
 * Created by janscheurenbrand on 18.08.14.
 */
public class TreeSetCookieStore {

    @GuardedBy("this")
    private TreeSet<Cookie> cookies;
    private String filepath;
    private boolean fileWasPresent;

    public TreeSetCookieStore(TreeSet<Cookie> cookies) {
        super();
        this.cookies = cookies;
    }

    /**
     * Adds an {@link Cookie HTTP cookie}, replacing any existing equivalent
     * cookies. If the given cookie has already expired it will not be added,
     * but existing values will still be removed.
     *
     * @param cookie the {@link Cookie cookie} to be added
     * @see #addCookies(Cookie[])
     */
    public synchronized void addCookie(final Cookie cookie) {
        if (cookie != null) {
            // first remove any old cookie that is equivalent
            cookies.remove(cookie);
            if (!cookie.isExpired(new Date())) {
                cookies.add(cookie);
            }
        }
    }

    /**
     * Adds an array of {@link Cookie HTTP cookies}. Cookies are added
     * individually and in the given array order. If any of the given cookies
     * has already expired it will not be added, but existing values will still
     * be removed.
     *
     * @param cookies the {@link Cookie cookies} to be added
     * @see #addCookie(Cookie)
     */
    public synchronized void addCookies(final Cookie[] cookies) {
        if (cookies != null) {
            for (final Cookie cooky : cookies) {
                this.addCookie(cooky);
            }
        }
    }

    /**
     * Returns an immutable array of {@link Cookie cookies} that this HTTP state
     * currently contains.
     *
     * @return an array of {@link Cookie cookies}.
     */
    public synchronized List<Cookie> getCookies() {
        // create defensive copy so it won't be concurrently modified
        return new ArrayList<Cookie>(cookies);
    }

    /**
     * Removes all of {@link Cookie cookies} in this HTTP state that have
     * expired by the specified {@link java.util.Date date}.
     *
     * @return true if any cookies were purged.
     * @see Cookie#isExpired(java.util.Date)
     */
    public synchronized boolean clearExpired(final Date date) {
        if (date == null) {
            return false;
        }
        boolean removed = false;
        for (final Iterator<Cookie> it = cookies.iterator(); it.hasNext(); ) {
            if (it.next().isExpired(date)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Clears all cookies.
     */
    public synchronized void clear() {
        cookies.clear();
    }

    @Override
    public synchronized String toString() {
        return cookies.toString();
    }
}

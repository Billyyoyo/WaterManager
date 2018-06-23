/*
* TimeSpan.java
*
* Created on January 28, 2003, 11:09 AM
* ====================================================================
*
* The JavaRanch Software License, Version 1.0
*
* Copyright (c) 2003 JavaRanch. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
* following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
* disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
* disclaimer in the documentation and/or other materials provided with the distribution.
*
* 3. The name JavaRanch must not be used to endorse or promote products derived from this software without prior written
* permission.
*
* 4. Products derived from this software may not be called "JavaRanch" nor may "JavaRanch" appear in their names without
* prior written permission of JavaRanch.
*
* THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JAVARANCH OR ITS
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
* BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE. ====================================================================
*
*/

package cn.eflo.managewatermeter.util;

import java.util.Date;

/**
 * The value of an instance of TimeSpan represents a period of time.
 * <p/>
 * TimeSpan can be used in several ways.
 * <p/>
 * To calculate the difference in time between two dates:
 * <PRE>TimeSpan timespan = TimeSpan.subtract(date1, date2);</PRE>
 * <p/>
 * To add five days to a TimeSpan:
 * <PRE>timspan.add(TimeSpanUnit.DAYS, 5);</PRE>
 * <p/>
 * To subtract another TimeSpan object from this one:
 * <PRE>timspan.subtract(timespan2);</PRE>
 *
 * @author Thomas Paul
 */
public class TimeSpan implements Comparable, java.io.Serializable, Cloneable {

    /**
     * Represents the Maximum TimeSpan value
     */
    public static final TimeSpan MAX_VALUE = new TimeSpan(Long.MAX_VALUE);

    /**
     * Represents the Minimum TimeSpan value
     */
    public static final TimeSpan MIN_VALUE = new TimeSpan(Long.MIN_VALUE);

    /**
     * Represents the TimeSpan with a value of zero
     */
    public static final TimeSpan ZERO = new TimeSpan(0L);

    private long time = 0;

    /**
     * Creates a new instance of TimeSpan based on the number of milliseconds
     * entered.
     *
     * @param time the number of milliseconds for this TimeSpan.
     */
    public TimeSpan(long time) {
        this.time = time;
    }

    /**
     * Creates a new TimeSpan object based on the unit and value entered.
     *
     * @param unit  the type of unit to use to create a TimeSpan instance.
     * @param value the number of units to use to create a TimeSpan instance.
     */
    public TimeSpan(TimeSpanUnit unit, long value) {
        this.time = TimeSpan.toMilliseconds(unit, value);
    }

    /**
     * Compares two TimeSpan objects.
     *
     * @param first  first TimeSpan to use in the compare.
     * @param second second TimeSpan to use in the compare.
     * @return a negative integer, zero, or a positive integer as the first
     * TimeSpan is less than, equal to, or greater than the
     * second TimeSpan.
     */
    public static int compare(TimeSpan first, TimeSpan second) {
        if (first.time == second.time) {
            return 0;
        }
        if (first.time > second.time) {
            return +1;
        }
        return -1;
    }

    /**
     * Subtracts two Date objects creating a new TimeSpan object.
     *
     * @param date1 Date to use as the base value.
     * @param date2 Date to subtract from the base value.
     * @return a TimeSpan object representing the difference bewteen the
     * two Date objects.
     */
    public static TimeSpan subtract(Date date1, Date date2) {
        return new TimeSpan(date1.getTime() - date2.getTime());
    }


    /**
     * Gets the number of days (truncated).
     *
     * @return the number of days.
     */
    public int getMonths() {

        long nDays = getDays();
        int nMonth = (int) (nDays / 30);
        if ((nDays % 30) != 0)
            nMonth++;

        return nMonth;
    }

    /**
     * Gets the number of days (truncated).
     *
     * @return the number of days.
     */
    public long getDays() {
        return (((this.time / 1000) / 60) / 60) / 24;
    }

    /**
     * Gets the number of hours (truncated).
     *
     * @return the number of hours.
     */
    public long getHours() {
        return ((this.time / 1000) / 60) / 60;
    }

    /**
     * Gets the number of milliseconds.
     *
     * @return the number of milliseconds.
     */
    public long getMilliseconds() {
        return this.time;
    }

    /**
     * Gets the number of minutes (truncated).
     *
     * @return the number of minutes.
     */
    public long getMinutes() {
        return (this.time / 1000) / 60;
    }

    /**
     * Returns a string for music formatting for album length.  So it would
     * look like 48:03 or 74:15.
     *
     * @return the string value of the music duration
     */

    public String getMusicDuration() {
        StringBuffer sb = new StringBuffer();
        try {
            long millis = this.time;
            if (millis < 0) {
                sb.append('-');
                millis = -millis;
            }

            long day = millis / TimeSpanUnit.TimeSpanConstants.DAYS;
            if (day != 0) {
                sb.append(day);
                sb.append("d.");
                millis = millis % TimeSpanUnit.TimeSpanConstants.DAYS;
            }

            long hours = millis / TimeSpanUnit.TimeSpanConstants.HOURS;

            if (hours != 0) {
                sb.append(hours);
                millis = millis % TimeSpanUnit.TimeSpanConstants.HOURS;
                sb.append(':');
            }

            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.MINUTES), 2, "0"));
            millis = millis % TimeSpanUnit.TimeSpanConstants.MINUTES;
            sb.append(':');
            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.SECONDS), 2, "0"));

        } catch (Exception e) {
            return null;
        }

        return sb.toString();
    }

    /**
     * Gets the number of seconds (truncated).
     *
     * @return the number of seconds.
     */
    public long getSeconds() {
        return this.time / 1000;
    }

    /**
     * Gets the number of days including fractional days.
     *
     * @return the number of days.
     */
    public double getTotalDays() {
        return (((this.time / 1000.0d) / 60.0d) / 60.0d) / 24.0d;
    }

    /**
     * Gets the number of hours including fractional hours.
     *
     * @return the number of hours.
     */
    public double getTotalHours() {
        return ((this.time / 1000.0d) / 60.0d) / 60.0d;
    }

    /**
     * Gets the number of minutes including fractional minutes.
     *
     * @return the number of minutes.
     */
    public double getTotalMinutes() {
        return (this.time / 1000.0d) / 60.0d;
    }

    /**
     * Gets the number of seconds including fractional seconds.
     *
     * @return the number of seconds.
     */
    public double getTotalSeconds() {
        return this.time / 1000.0d;
    }

    /**
     * Indicates whether the value of the TimeSpan is negative.
     *
     * @return <code>true</code> if the value of the TimeSpan is less
     * than zero.
     * <code>false</code> otherwise.
     */
    public boolean isNegative() {
        return (this.compareTo(TimeSpan.ZERO) < 0) ? true : false;
    }

    /**
     * Indicates whether the value of the TimeSpan is positive.
     *
     * @return <code>true</code> if the value of the TimeSpan is greater
     * than zero.
     * <code>false</code> otherwise.
     */
    public boolean isPositive() {
        return (this.compareTo(TimeSpan.ZERO) > 0) ? true : false;
    }

    /**
     * Indicates whether the value of the TimeSpan is zero.
     *
     * @return <code>true</code> if the value of the TimeSpan is equal to zero.
     * <code>false</code> otherwise.
     */
    public boolean isZero() {
        return this.equals(TimeSpan.ZERO);
    }

    /**
     * Adds a TimeSpan to this TimeSpan.
     *
     * @param timespan the TimeSpan to add to this TimeSpan.
     */
    public void add(TimeSpan timespan) {
        add(TimeSpanUnit.MILLISECONDS, timespan.time);
    }

    /**
     * Adds a number of units to this TimeSpan.
     *
     * @param unit  the type of unit to add to this TimeSpan.
     * @param value the number of units to add to this TimeSpan.
     */
    public void add(TimeSpanUnit unit, long value) {
        this.time += TimeSpan.toMilliseconds(unit, value);
    }

    /**
     * Returns a clone of this TimeSpan.
     *
     * @return a clone of this TimeSpan.
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object. Comparison is
     * based on the number of milliseconds in this TimeSpan.
     *
     * @param o the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it
     *                            from being compared to this Object.
     */
    public int compareTo(Object o) {
        TimeSpan compare = (TimeSpan) o;
        if (this.time == compare.time) {
            return 0;
        }
        if (this.time > compare.time) {
            return +1;
        }
        return -1;
    }

    /**
     * Returns a TimeSpan whose value is the absolute value of this TimeSpan.
     *
     * @return a TimeSpan whose value is the absolute value of this TimeSpan.
     */
    public TimeSpan duration() {
        return new TimeSpan(Math.abs(this.time));
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Comparison is based on the number of milliseconds in this TimeSpan.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if the obj argument is a TimeSpan object
     * with the exact same number of milliseconds.
     * <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof TimeSpan) {
            TimeSpan compare = (TimeSpan) obj;
            if (this.time == compare.time) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>. The method uses the same
     * algorithm as found in the Long class.
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see java.util.Hashtable
     */
    public int hashCode() {
        return new Long(this.time).hashCode();
    }

    /**
     * Returns a TimeSpan whose value is the negated value of this TimeSpan.
     *
     * @return a TimeSpan whose value is the negated value of this TimeSpan.
     */
    public TimeSpan negate() {
        return new TimeSpan(-this.time);
    }

    /**
     * Subtracts a TimeSpan from this TimeSpan.
     *
     * @param timespan the TimeSpan to subtract from this TimeSpan.
     */
    public void subtract(TimeSpan timespan) {
        subtract(TimeSpanUnit.MILLISECONDS, timespan.time);
    }

    /**
     * Subtracts a number of units from this TimeSpan.
     *
     * @param unit  the type of unit to subtract from this TimeSpan.
     * @param value the number of units to subtract from this TimeSpan.
     */
    public void subtract(TimeSpanUnit unit, long value) {
        add(unit, -value);
    }

//    public String formatDayTime()
//    {
//         long seconds = (long)Math.floor(getTotalSeconds());
//         long minutes = (long)Math.floor(getTotalSeconds() / 60.0);
//         long hours = (long)Math.floor(minutes / 60.0);
//         long days  = (long)Math.floor(hours / 24.0);
//
//         if (days != 0)
//         {
//            return days + " day " + (hours % 24) + " hr ago";// + " min ago";// + (seconds % 60) + "seconds";
//         }
//         else if(hours != 0)
//         {
//           return hours + " hr " + (minutes % 60) + " min ago";// + (seconds % 60) + "seconds";
//         }
//         else if(minutes % 60 != 0)
//         {
//           return (minutes % 60) + " min " + (seconds % 60) + " sec ago";
//         }
//         else
//         {
//            return (seconds % 60) + " sec ago";
//         }
//    }

//    public String formatDayTime() {
//        return formatDayTime((long) getTotalSeconds(), 0);
//    }

//    public static String formatDayTime(long timeInSeconds, int nDecimal) {
//        if (timeInSeconds < 0)
//            timeInSeconds = 1;
//
//        long seconds = (long) Math.floor(timeInSeconds);
//        long minutes = (long) Math.floor(timeInSeconds / 60.0);
//        long hours = (long) Math.floor(minutes / 60.0);
//        long days = (long) Math.floor(hours / 24.0);
//
//        String sPointSec = Utilities.getNumberFormat(timeInSeconds, 'N', nDecimal);
//        int nIndex = sPointSec.lastIndexOf(".");
//        if (nIndex != -1)
//            sPointSec = sPointSec.substring(nIndex);
//        else
//            sPointSec = "";
//
//        if (days != 0) {
//            return days + " day " + (hours % 24) + " hr " + (minutes % 60) + " min " + (seconds % 60) + sPointSec + " sec";
//        } else if (hours != 0) {
//            return hours + " hr " + (minutes % 60) + " min " + (seconds % 60) + sPointSec + " sec";
//        } else if (minutes % 60 != 0) {
//            return (minutes % 60) + " min " + (seconds % 60) + sPointSec + " sec";
//        } else {
//            return (seconds % 60) + sPointSec + " sec";
//        }
//    }

    public String formatDayTime2() {
        double dTotal = getTotalSeconds();
        if (dTotal < 0)
            dTotal = 1;

        long seconds = (long) Math.floor(dTotal);
        long minutes = (long) Math.floor(dTotal / 60.0);
        long hours = (long) Math.floor(minutes / 60.0);
        long days = (long) Math.floor(hours / 24.0);

        if (days != 0) {
            if (days > 90)
                return (days / 30) + " month " + ((days % 30) > 0 ? ((days % 30) + " day ago") : "ago");
            else
                return days + " day " + ((hours % 24) > 0 ? ((hours % 24) + " hr ago") : "ago");
        } else if (hours != 0) {
            return hours + " hr " + ((minutes % 60) > 0 ? ((minutes % 60) + " min ago") : "ago");
        } else if (minutes % 60 != 0) {
            return (minutes % 60) + " min " + ((seconds % 60) > 0 ? ((seconds % 60) + " sec ago") : "ago");
        } else {
            if ((seconds % 60) > 50)
                //return (seconds % 60) + " seconds";
                return "1 min ago";
            else
                return ((seconds % 60) + 1) + " sec ago";
        }
    }

//    public static String getDateTimeAgo(String sDateTime, String sFormat, boolean bExactly) {
//        try {
//            Date lastDate;
//            if (sFormat != null)
//                lastDate = Utilities.parseDate(sFormat, sDateTime);
//            else
//                lastDate = new Date(Utilities.getLong(sDateTime, 0));
//
//            Date currentDate = Calendar.getInstance().getTime();
//            if (currentDate.compareTo(lastDate) >= 0) {
//                TimeSpan timeSpan = TimeSpan.subtract(currentDate, lastDate);
//                if (bExactly)
//                    return timeSpan.formatDayTime();
//                else
//                    return timeSpan.formatDayTime2();
//            } else {
//                TimeSpan timeSpan = TimeSpan.subtract(lastDate, currentDate);
//                if (bExactly)
//                    return "+" + timeSpan.formatDayTime();
//                else
//                    return "+" + timeSpan.formatDayTime2();
//            }
//
//        } catch (Exception e) {
//        }
//
//        return "7 day ago";
//    }

    /**
     * Returns a string representation of the object in the format
     * "[-]d.hh:mm:ss.ff" where "-" is an optional sign for negative TimeSpan
     * values, the "d" component is days, "hh" is hours, "mm" is minutes,
     * "ss" is seconds, and "ff" is milliseconds
     *
     * @return a string containing the number of milliseconds.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            long millis = this.time;
            if (millis < 0) {
                sb.append('-');
                millis = -millis;
            }

            long day = millis / TimeSpanUnit.TimeSpanConstants.DAYS;

            if (day != 0) {
                sb.append(day);
                sb.append("d.");
                millis = millis % TimeSpanUnit.TimeSpanConstants.DAYS;
            }

            sb.append(millis / TimeSpanUnit.TimeSpanConstants.HOURS);
            millis = millis % TimeSpanUnit.TimeSpanConstants.HOURS;
            sb.append("h:");
            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.MINUTES), 2, "0"));
            millis = millis % TimeSpanUnit.TimeSpanConstants.MINUTES;
            sb.append("m:");
            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.SECONDS), 2, "0"));
            sb.append('s');
            millis = millis % TimeSpanUnit.TimeSpanConstants.SECONDS;
            if (millis != 0) {
                sb.append('.');
                sb.append(millis);
                sb.append("ms");
            }

        } catch (Exception e) {
        }

        return sb.toString();
    }

    private static long toMilliseconds(TimeSpanUnit unit, long value) {
        return value * unit.getValue();
    }

    /**
     * Gets the time.
     * <p/>
     *
     * @return Returns the time.
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Sets the time.
     * <p/>
     *
     * @param aTime The time to set.
     */
    public void setTime(long aTime) {
        this.time = aTime;
    }

    public static void main(String[] args) {
//           String sTimeAgo = getDateTimeAgo("2011-10-02 08:50:01", "yyyy-MM-dd HH:mm:ss", true);
//System.out.println("sTimeAgo=" + sTimeAgo);
//        String sDateTime = "2012-07-03 19:34:17";
//
//        Date updateDate;
//        if (sDateTime.length() >= 19)
//            updateDate = Utilities.parseDate("yyyy-MM-dd HH:mm:ss", sDateTime.substring(0, 19));
//        else if (sDateTime.length() < 14)
//            updateDate = Utilities.parseDate("yyyyMMdd HH:mm:ss", sDateTime.substring(0, 8) + Utilities.getTodayDateTime().substring(10));
//        else
//            updateDate = Utilities.parseDate("yyyyMMddHHmmss", sDateTime.substring(0, 14));
//
//        Date currentDate = Calendar.getInstance().getTime();
//        TimeSpan timeSpan = TimeSpan.subtract(currentDate, updateDate);
//        String sAgo = timeSpan.formatDayTime2();
//
//        System.out.println("sAgo=" + sAgo + ", " + (44 % 24));

    }

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if ((padLen == 1) && (pads <= 8192)) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen)
            return padStr.concat(str);
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }
        return new String(padding).concat(str);
    }

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }
}

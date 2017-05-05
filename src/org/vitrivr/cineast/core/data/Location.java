package org.vitrivr.cineast.core.data;

import static com.google.common.base.Preconditions.checkArgument;

import com.drew.lang.GeoLocation;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Location implements ReadableFloatVector {
  private static final int ELEMENT_COUNT = 2;

  private static final Logger logger = LogManager.getLogger();

  private final float latitude;
  private final float longitude;

  private Location(float latitude, float longitude) {
    this.latitude = clampLatitude(latitude);
    this.longitude = wrapLongitude(longitude);
  }

  private float clampLatitude(float latitude) {
    float clamped = Math.max(-90f, Math.min(90f, latitude));
    if (latitude < -90f || latitude > 90f) {
      logger.warn("Latitude value must lie between [-90,90] but found {}, clamping to {}",
          latitude, clamped);
    }
    return clamped;
  }

  private float wrapLongitude(float longitude) {
    float wrapped = (longitude % 360f + 360f) % 360f;
    wrapped = wrapped < 180f ? wrapped : wrapped - 360f;
    if (longitude < -180f || longitude >= 180f) {
      logger.warn("Longitude value must lie between [-180,180) but found {}, wrapping around to {}",
          longitude, wrapped);
    }
    return wrapped;
  }

  public static Location of(float latitude, float longitude) {
    return new Location(latitude, longitude);
  }

  public static Location of(GeoLocation geoLocation) {
    return of((float) geoLocation.getLatitude(), (float) geoLocation.getLongitude());
  }

  public static Location of(float[] array) {
    checkArgument(array.length == ELEMENT_COUNT,
        "Float array must contain %s elements, but found %s.", ELEMENT_COUNT, array.length);
    return of(array[0], array[1]);
  }

  public float getLatitude() {
    return latitude;
  }

  public float getLongitude() {
    return longitude;
  }

  public String toString() {
    return "(" + this.getLatitude() + ", " + this.getLongitude() + ")";
  }

  /* ReadableFloatVector */
  @Override
  public int getElementCount() {
    return ELEMENT_COUNT;
  }

  @Override
  public float getElement(int num) {
    switch (num) {
      case 0: return this.getLatitude();
      case 1: return this.getLongitude();
      default: throw new IndexOutOfBoundsException(num + " >= " + this.getElementCount());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Float.compare(location.latitude, latitude)   == 0
        && Float.compare(location.longitude, longitude) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(latitude, longitude);
  }
}
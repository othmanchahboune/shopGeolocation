package com.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import com.common.Status;
import com.entities.shop.ShopLikingStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Useful methods to handle entities information
 *
 */
@Slf4j
public class EntityHelper {

	public static final String CREATION_DATE_PARAMETER = "creationDate";
	public static final String UPDATE_DATE_PARAMETER = "updateDate";
	public static final String STATUS_DATE_PARAMETER = "statusDate";
	public static final String LIKE_STATUS_DATE_PARAMETER = "likeStatusDate";
	public static final String STATUS_PARAMETER = "status";
	public static final String LIKE_STATUS_PARAMETER ="likeStatus";

	private EntityHelper() {

	}

	@SuppressWarnings("unchecked")
	public static <T> T getParameterSecure(final Object object, final String property) {
		try {
			final Object obj = getParameterValue(object, property);

			if (obj != null)
				return (T) obj;
		} catch (final Exception e) {
			log.warn(e.getMessage());
		}

		return null;
	}

	/**
	 * Set dates information for the given entity
	 * 
	 * @param entity on which we set: <br>
	 *               {@value #CREATION_DATE_PARAMETER} <br>
	 *               {@value #UPDATE_DATE_PARAMETER} <br>
	 *               {@value #STATUS_DATE_PARAMETER} <br>
	 *               {@value #LIKE_STATUS_DATE_PARAMETER} <br>
	 *               {@value #STATUS_PARAMETER}
	 */
	public static void createEntityDatesInformation(Object entity) {
		if (null == entity) {
			log.warn("Cannot process <null> entity.");
			return;
		}

		LocalDateTime now = LocalDateTime.now();

		Status status = getParameterSecure(entity, STATUS_PARAMETER);

		if (null == status)
			status = Status.ACTIVE;

		setParameterValue(entity, STATUS_PARAMETER, status);
		setParameterValue(entity, CREATION_DATE_PARAMETER, now);
		setParameterValue(entity, UPDATE_DATE_PARAMETER, now);
		setParameterValue(entity, STATUS_DATE_PARAMETER, now);
		setParameterValue(entity, LIKE_STATUS_DATE_PARAMETER, now);

	}

	/**
	 * Set current date information for the given entity in a update process
	 * 
	 * @param entityToUpdate The entity to update.
	 */
	public static void updateEntityDatesInformation(final Object entityToUpdate) {
		if (null == entityToUpdate) {

			log.warn("Cannot process <null> entity.");
			return;
		}
		setParameterValue(entityToUpdate, UPDATE_DATE_PARAMETER, LocalDateTime.now());
	}
	 /**
     * Set dates information for the given entity in comparison with the previous
     * entity
     * 
     * @param entityToUpdate on which we set: <br> {@value #UPDATE_DATE_PARAMETER}
     * <br> {@value #STATUS_DATE_PARAMETER}
     */
    public static void updateEntityDatesInformation(Object entityToUpdate, Object previousEntity) {
        if (null == entityToUpdate || null == previousEntity) {

            log.warn("Cannot process <null> entity.");
            return;
        }
        
        LocalDateTime now = LocalDateTime.now();
        if(getParameterSecure(entityToUpdate, LIKE_STATUS_PARAMETER)!=null) {
            final ShopLikingStatus newStatus = getParameterSecure(entityToUpdate, LIKE_STATUS_PARAMETER);
            final ShopLikingStatus previousStatus = getParameterSecure(previousEntity, LIKE_STATUS_PARAMETER);

            if (newStatus != null && previousStatus != null && newStatus != previousStatus)
                setParameterValue(entityToUpdate, LIKE_STATUS_DATE_PARAMETER, now);
        }
        if(getParameterSecure(entityToUpdate, STATUS_PARAMETER)!=null){
            final Status newStatus = getParameterSecure(entityToUpdate, STATUS_PARAMETER);
            final Status previousStatus = getParameterSecure(previousEntity, STATUS_PARAMETER);

            if (newStatus != null && previousStatus != null && newStatus != previousStatus)
                setParameterValue(entityToUpdate, STATUS_DATE_PARAMETER, now);
         }

        setParameterValue(entityToUpdate, UPDATE_DATE_PARAMETER, now);

    }
	/**
	 * Get the parameter value
	 *
	 * @param entity
	 * @param property
	 * @return The parameter value from the object, or RuntimeException if the
	 *         extraction fails
	 */
	public static Object getParameterValue(Object entity, String property) {

		String methodName = "getParameterValue";

		// Worst cases
		if (null == property || null == entity) {
			return null;
		}

		try {
			Field field = entity.getClass().getDeclaredField(property);
			field.setAccessible(true);
			return field.get(entity);

		} catch (Exception ex) {

			log.info(methodName + " - exception: " + ex.getMessage()
					+ ". Cannot get the value of the following parameter : " + property + " for entity : "
					+ entity.toString());
			return null;
		}

	}

	/**
	 * Set the parameter value in the given entity
	 *
	 * @param entity
	 * @param property
	 * @param value
	 * @return
	 */
	public static boolean setParameterValue(Object entity, String property, Object value) {

		String methodName = "setParameterValue";

		// Worst cases
		if (null == property || null == entity) {
			return false;
		}

		try {
			Field field = entity.getClass().getDeclaredField(property);
			field.setAccessible(true);

			Class<?> type = field.getType();
			Class<?> valueType = value.getClass();

			if (valueType.equals(type)) {
				field.set(entity, value);
			} else {
				throw new UnsupportedOperationException("Cannot set " + valueType.getName() + " to " + type.getName());
			}

		} catch (Exception ex) {
			log.info(methodName + " - exception: " + ex.getMessage() + ". Cannot set parameter : " + property
					+ " for entity : " + entity.toString());
			return false;
		}

		return true;
	}

	
}

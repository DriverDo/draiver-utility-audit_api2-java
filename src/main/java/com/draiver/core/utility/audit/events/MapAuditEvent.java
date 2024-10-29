package com.draiver.core.utility.audit.events;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapAuditEvent extends AuditEventBase {

	private static final long serialVersionUID = 7628514914497581902L;

	private static final String TYPE_NAME = "MapAuditEvent";
	private static final List<String> SPECIAL_KEYS = new ArrayList<>();

	public MapAuditEvent(String json) {
		this(null, json);
	}

	public MapAuditEvent(EventConfig eventConfig, String json) {
		super(eventConfig, (List<String>) null);
		initiailze();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, String> map = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {
			});
			processMap(map);
		} catch (Exception e) {
			// eat exception
		}
	}

	public MapAuditEvent(Map<String, String> map) {
		this(null, map);
	}

	public MapAuditEvent(EventConfig eventConfig, Map<String, String> map) {
		super(eventConfig, (List<String>) null);
		initiailze();
		processMap(map);
	}

	public MapAuditEvent(AuditEvent auditEvent) {
		this(auditEvent.toJson());
	}

	private void processMap(Map<String, String> value) {
		try {
			for (Entry<String, String> entry : value.entrySet()) {
				if (entry != null) {
					if (isSpecialParameter(entry)) {
						processSpecialParameters(entry);
					}
					else if (!StringUtils.isEmpty(entry.getValue())) {
						safeUpdateInternalParameter(entry.getKey(), entry.getValue());
					}
				}
			}
		} catch (Exception e) {
			// eat exception
		}

		if (getEventName().isEmpty()) {
			setEventName(TYPE_NAME);
		}

		if (getEventType().isEmpty()) {
			setEventType(TYPE_NAME);
		}
	}

	private void processSpecialParameters(Entry<String, String> item) {

		if (item == null) {
			return;
		}

		String key = item.getKey().toLowerCase();

		if (key.equalsIgnoreCase(EVENT_CREATED)) {
			processSpecialParametersEventCreated(item);
			return;
		}

		if (key.equalsIgnoreCase(EVENT_LEVEL)) {
			processSpecialParametersEventLevel(item);
			return;
		}

		if (key.equalsIgnoreCase(EVENT_STATUS)) {
			processSpecialParametersEventStatus(item);
			return;
		}

		if (key.equalsIgnoreCase(EVENT_ID) || key.equalsIgnoreCase(EVENT_TIMESTAMP)) {
			processSpecialParametersByKey(item, key);
		}

	}

	private void processSpecialParametersEventCreated(Entry<String, String> item) {
		try {
			ZonedDateTime dateTime = ZonedDateTime.parse(item.getValue(), DateTimeFormatter.ofPattern(DATETIME_FORMAT));
			safeUpdateEventCreated(dateTime);
		} catch (Exception e) {
			// eat exception
		}

	}

	private void processSpecialParametersEventLevel(Entry<String, String> item) {
		try {
			setEventLevel(AuditEventLevel.valueOf(item.getValue()));
		} catch (Exception e) {
			// eat exception
		}
	}

	private void processSpecialParametersEventStatus(Entry<String, String> item) {
		try {
			setEventStatus(AuditEventStatus.valueOf(item.getValue()));
		} catch (Exception e) {
			// eat exception
		}
	}

	private void processSpecialParametersByKey(Entry<String, String> item, String key) {
		try {
			safeUpdateInternalParameter(key, item.getValue());
		} catch (Exception e) {
			// eat exception
		}
	}

	private boolean isSpecialParameter(Entry<String, String> item) {
		if (item == null) {
			return false;
		}

		return SPECIAL_KEYS.contains(item.getKey().toLowerCase());
	}

	private void initiailze() {
		SPECIAL_KEYS.add(EVENT_CREATED);
		SPECIAL_KEYS.add(EVENT_ID);
		SPECIAL_KEYS.add(EVENT_LEVEL);
		SPECIAL_KEYS.add(EVENT_STATUS);
	}

}

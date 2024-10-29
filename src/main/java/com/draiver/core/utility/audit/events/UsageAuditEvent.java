package com.draiver.core.utility.audit.events;

public class UsageAuditEvent extends GenericAuditEvent {

	private static final long serialVersionUID = -6988260499978270417L;

	private static final String EVENTCATEGORY = "AUDIT";
	private static final String EVENTNAME = "Usage";
	private static final String TYPE_NAME = "UsageAuditEvent";

	private static final String PARAM_USAGE_AMOUNT = "UsageAmount";
	private static final String PARAM_USAGE_ITEM = "UsageItem";
	private static final String PARAM_USAGE_ITEM_PROVIDER = "UsageItemProvider";
	private static final String PARAM_USAGE_ITEM_DATA = "UsageItemData";

	public UsageAuditEvent() {
		super(EVENTNAME, TYPE_NAME, null, AuditEventLevel.INFO, AuditEventStatus.SUCCESS);
		this.setEventCategory(EVENTCATEGORY);
	}

	public double getUsageAmount() {
		try {
			return Double.parseDouble(safeGetExtraParameter(PARAM_USAGE_AMOUNT));
		} catch (Exception ex) {
			return 0;
		}
	}

	public String getUsageItem() {
		return safeGetExtraParameter(PARAM_USAGE_ITEM);
	}

	public void setUsageAmount(double value) {
		safeAddExtraParameter(PARAM_USAGE_AMOUNT, Double.toString(value));
		updateMessage();
	}

	public void setUsageItem(String value) {
		safeAddExtraParameter(PARAM_USAGE_ITEM, value);
		updateMessage();
	}

	public String getUsageItemProvider() {
		return safeGetExtraParameter(PARAM_USAGE_ITEM_PROVIDER);
	}

	public void setUsageItemProvider(String value) {
		safeAddExtraParameter(PARAM_USAGE_ITEM_PROVIDER, value);
		updateMessage();
	}

	public String getUsageItemData() {
		return safeGetExtraParameter(PARAM_USAGE_ITEM_DATA);
	}

	public void setUsageItemData(String value) {
		safeAddExtraParameter(PARAM_USAGE_ITEM_DATA, value);
		updateMessage();
	}

	private void updateMessage() {
		setMessage(String.format("%s:%s x %s", getUsageItemProvider(), getUsageItem(), getUsageAmount()));
	}

}

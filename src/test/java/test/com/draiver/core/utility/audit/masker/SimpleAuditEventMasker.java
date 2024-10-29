package test.com.draiver.core.utility.audit.masker;

import com.draiver.core.utility.audit.masker.JsonOnlyAuditEventMaskerBase;

public class SimpleAuditEventMasker extends JsonOnlyAuditEventMaskerBase {

	public SimpleAuditEventMasker() {		
	}

	@Override
	protected String onMaskToJson(String json) {
		return "{'test':'****'}";
	}

}

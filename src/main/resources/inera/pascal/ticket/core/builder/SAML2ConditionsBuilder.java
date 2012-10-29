package se.inera.pascal.ticket.core.builder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.impl.ConditionsBuilder;

import se.inera.pascal.ticket.core.Builder;

public class SAML2ConditionsBuilder implements Builder<Conditions> {
	
//	private DateTime notBefore    = new DateTime(2010,11,5,9,0,0,50,DateTimeZone.UTC);
//	private DateTime notOnOrAfter = new DateTime(2010,11,5,9,0,0,50,DateTimeZone.UTC).plusHours(1);	
	private DateTime notBefore    = new DateTime(DateTimeZone.UTC);
	private DateTime notOnOrAfter = new DateTime(DateTimeZone.UTC).plusHours(1);	
	private List<Condition> conditionsList = new ArrayList<Condition>();
	
	public SAML2ConditionsBuilder setNotBefore(DateTime notBefore) {
		this.notBefore = notBefore;
		return this;
	}

	public SAML2ConditionsBuilder setNotOnOrAfter(DateTime notOnOrAfter) {
		this.notOnOrAfter = notOnOrAfter;
		return this;
	}
	
	public SAML2ConditionsBuilder addCondition(Condition condition){
		conditionsList.add(condition);
		return this;
	}

	@Override
	public Conditions build() {
		Conditions conditions = new ConditionsBuilder().buildObject();
		conditions.setNotBefore(notBefore);
		conditions.setNotOnOrAfter(notOnOrAfter);
		for (Condition condition : conditionsList) {
			conditions.getConditions().add(condition);
		}
		return conditions;
	}
}

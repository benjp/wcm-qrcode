@org.juzu.Application
@Bindings(
   @Binding(value = org.exoplatform.services.organization.OrganizationService.class, implementation=GateInMetaProvider.class)
)
package org.gatein.portal.people;

import org.juzu.impl.inject.Binding;
import org.juzu.impl.inject.Bindings;
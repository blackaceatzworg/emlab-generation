/*******************************************************************************
 * Copyright 2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package emlab.gen.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import emlab.gen.domain.gis.Zone;
import emlab.gen.domain.policy.renewablesupport.RenewableSupportSchemeTender;
import emlab.gen.domain.policy.renewablesupport.TenderBid;

/**
 * @author rjjdejeu
 *
 */
public interface TenderBidRepository extends GraphRepository<TenderBid> {

    // This sorts the submitted tender bids by price
    @Query(value = "g.idx('__types__')[[className:'emlab.gen.domain.policy.renewablesupport.TenderBid.java']].filter{it.time == tick}.sort{it.a.price}._()", type = QueryType.Gremlin)
    public Iterable<TenderBid> findAllSortedTenderBidsbyTime(@Param("tick") long time);

    // this returns the accepted tender bids Scheme --with regulator-->
    // Regulator --of zone--> Zone
    @Query(value = "g.v(scheme).out(´WITH_REGULATOR').out('OF_ZONE').propertyFilter('time', FilterPipe.Filter.EQUAL, time).propertyFilter('status', FilterPipe.Filter.GREATER_THAN_EQUAL, 2)", type = QueryType.Gremlin)
    public Iterable<TenderBid> findAllAcceptedTenderBids(
            @Param("scheme") RenewableSupportSchemeTender renewableSupportSchemeTender, @Param("zone") Zone zone,
            @Param("time") long time);

}
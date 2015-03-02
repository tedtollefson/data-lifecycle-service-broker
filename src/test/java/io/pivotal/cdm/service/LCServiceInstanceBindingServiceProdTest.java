package io.pivotal.cdm.service;

import static io.pivotal.cdm.config.LCCatalogConfig.PRODUCTION;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import io.pivotal.cdm.provider.CopyProvider;
import io.pivotal.cdm.repo.BrokerActionRepository;

import java.util.*;

import org.cloudfoundry.community.servicebroker.exception.*;
import org.cloudfoundry.community.servicebroker.model.*;
import org.junit.*;
import org.mockito.*;

import com.amazonaws.services.ec2.AmazonEC2Client;

public class LCServiceInstanceBindingServiceProdTest {

	@Mock
	AmazonEC2Client ec2Client;

	private LCServiceInstanceBindingService bindingService;

	CreateServiceInstanceRequest createServiceInstanceRequest = new CreateServiceInstanceRequest(
			"test_service", PRODUCTION, "org", "space")
			.withServiceInstanceId("test_service_id");
	private ServiceInstance serviceInstance = new ServiceInstance(
			createServiceInstanceRequest);

	private String serviceId = "postgrescmd";

	@Mock
	CopyProvider provider;

	private static String bindingId = "test_binding";

	@Mock
	LCServiceInstanceService instanceService;

	@Mock
	BrokerActionRepository repo;

	@Mock
	LCServiceInstanceBindingManager bindingManager;

	@Before
	public void setUp() throws ServiceInstanceBindingExistsException,
			ServiceBrokerException {
		MockitoAnnotations.initMocks(this);
		bindingService = new LCServiceInstanceBindingService(provider,
				instanceService, repo, bindingManager);
	}

	@Test
	public void itShouldReturnTheProdCreds() throws ServiceBrokerException,
			ServiceInstanceBindingExistsException {

		Map<String, Object> testCreds = new HashMap<String, Object>();
		testCreds.put("uri", "prod_uri");
		when(provider.getCreds("source_instance")).thenReturn(testCreds);

		ServiceInstanceBinding bindResult = bindingService
				.createServiceInstanceBinding(new CreateServiceInstanceBindingRequest(
						serviceInstance.getServiceDefinitionId(), PRODUCTION,
						"test_app")
						.withBindingId(bindingId)
						.and()
						.withServiceInstanceId(
								serviceInstance.getServiceInstanceId()));
		assertThat(bindResult.getId(), is(equalTo(bindingId)));
	}

	@Test
	public void itShouldNotInteractWithProviderForTheProductionCopyDuringUnbind()
			throws ServiceBrokerException {
		bindingService
				.deleteServiceInstanceBinding(new DeleteServiceInstanceBindingRequest(
						bindingId, serviceInstance, serviceId, PRODUCTION));
		verify(provider, never()).deleteCopy(any());
	}
}
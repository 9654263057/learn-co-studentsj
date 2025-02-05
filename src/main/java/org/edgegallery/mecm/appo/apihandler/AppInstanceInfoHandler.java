/*
 *  Copyright 2020 Huawei Technologies Co., Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.edgegallery.mecm.appo.apihandler;

import static org.edgegallery.mecm.appo.common.Constants.APP_INST_ID_REGX;
import static org.edgegallery.mecm.appo.common.Constants.TENENT_ID_REGEX;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.LinkedList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.edgegallery.mecm.appo.apihandler.dto.AppInstanceInfoDto;
import org.edgegallery.mecm.appo.model.AppInstanceInfo;
import org.edgegallery.mecm.appo.service.AppInstanceInfoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Application instance info API handler.
 */
@Api(value = "Application instance info api system")
@Validated
@RequestMapping("/appo/v1")
@RestController
public class AppInstanceInfoHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppInstanceInfoHandler.class);

    private AppInstanceInfoService appInstanceInfoService;

    @Autowired
    public AppInstanceInfoHandler(AppInstanceInfoService appInstanceInfoService) {
        this.appInstanceInfoService = appInstanceInfoService;
    }

    /**
     * Retrieves application instance information.
     *
     * @param accessToken   access token
     * @param tenantId      tenant ID
     * @param appInstanceId application instance ID
     * @return application instance information
     */
    @ApiOperation(value = "Retrieves application instance info", response = AppInstanceInfoDto.class)
    @GetMapping(path = "/tenants/{tenant_id}/app_instance_infos/{appInstance_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppInstanceInfoDto> getAppInstanceInfo(
            @ApiParam(value = "access token")
            @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "tenant id") @PathVariable("tenant_id")
            @Pattern(regexp = TENENT_ID_REGEX) String tenantId,
            @ApiParam(value = "application instance id") @PathVariable("appInstance_id")
            @Pattern(regexp = APP_INST_ID_REGX) String appInstanceId) {

        logger.info("Retrieve application instance info: {}", appInstanceId);

        AppInstanceInfo appInstanceInfo = appInstanceInfoService.getAppInstanceInfo(tenantId, appInstanceId);
        ModelMapper mapper = new ModelMapper();
        AppInstanceInfoDto dto = mapper.map(appInstanceInfo, AppInstanceInfoDto.class);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Retrieves application instance information.
     *
     * @param accessToken access token
     * @param tenantId    tenant ID
     * @return application instance information
     */
    @ApiOperation(value = "Retrieves application instance info", response = List.class)
    @GetMapping(value = "/tenants/{tenant_id}/app_instance_infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppInstanceInfoDto>> getAllAppInstanceInfo(
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "tenant id") @PathVariable("tenant_id")
            @Pattern(regexp = TENENT_ID_REGEX) String tenantId) {

        logger.info("Retrieve application instance infos");

        List<AppInstanceInfoDto> appInstanceInfosDto = new LinkedList<>();
        List<AppInstanceInfo> appInstanceInfos = appInstanceInfoService.getAllAppInstanceInfo(tenantId);
        for (AppInstanceInfo tenantAppInstanceInfo : appInstanceInfos) {
            ModelMapper mapper = new ModelMapper();
            appInstanceInfosDto.add(mapper.map(tenantAppInstanceInfo, AppInstanceInfoDto.class));
        }

        return new ResponseEntity<>(appInstanceInfosDto, HttpStatus.OK);
    }

    /**
     * Creates application instance information.
     *
     * @param accessToken    access token
     * @param tenantId       tenant ID
     * @param appInstInfoDto application instance info
     * @return application instance information
     */
    @ApiOperation(value = "Creates application instance info", response = AppInstanceInfoDto.class)
    @PostMapping(path = "/tenants/{tenant_id}/app_instance_infos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppInstanceInfoDto> createAppInstanceInfo(
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "tenant id") @PathVariable("tenant_id")
            @Pattern(regexp = TENENT_ID_REGEX) String tenantId,
            @ApiParam(value = "application instance") @RequestBody AppInstanceInfoDto appInstInfoDto) {

        logger.info("Create application instance info: {}", appInstInfoDto.getAppInstanceId());

        ModelMapper mapper = new ModelMapper();
        AppInstanceInfo appInstanceInfo = mapper.map(appInstInfoDto, AppInstanceInfo.class);

        appInstanceInfo = appInstanceInfoService.createAppInstanceInfo(tenantId, appInstanceInfo);

        AppInstanceInfoDto dto = mapper.map(appInstanceInfo, AppInstanceInfoDto.class);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    /**
     * Deletes application instance information.
     *
     * @param accessToken   access token
     * @param tenantId      tenant ID
     * @param appInstanceId application instance ID
     */
    @ApiOperation(value = "Deletes application instance info", response = String.class)
    @DeleteMapping(path = "/tenants/{tenant_id}/app_instance_infos/{appInstance_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteAppInstanceInfo(
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "tenant id") @PathVariable("tenant_id")
            @Pattern(regexp = TENENT_ID_REGEX) String tenantId,
            @ApiParam(value = "application instance id") @PathVariable("appInstance_id")
            @Pattern(regexp = APP_INST_ID_REGX) String appInstanceId) {

        logger.info("Delete application instance info: {}", appInstanceId);
        appInstanceInfoService.deleteAppInstanceInfo(tenantId, appInstanceId);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * Updates application instance information.
     *
     * @param accessToken access token
     * @param tenantId    tenant ID
     * @param instInfo    application instance info
     * @return application instance information
     */
    @ApiOperation(value = "Creates application instance info", response = AppInstanceInfoDto.class)
    @PutMapping(path = "/tenants/{tenant_id}/app_instance_infos/{appInstance_id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppInstanceInfoDto> updateAppInstanceInfo(
            @ApiParam(value = "access token") @RequestHeader("access_token") String accessToken,
            @ApiParam(value = "tenant id") @PathVariable("tenant_id")
            @Pattern(regexp = TENENT_ID_REGEX) String tenantId,
            @ApiParam(value = "application instance id") @PathVariable("appInstance_id")
            @Pattern(regexp = APP_INST_ID_REGX) String appInstanceId,
            @ApiParam(value = "application instance") @Valid @RequestBody AppInstanceInfoDto instInfo) {

        logger.info("Update application instance info: {}", instInfo.getAppInstanceId());

        ModelMapper mapper = new ModelMapper();
        AppInstanceInfo appInstanceInfo = mapper.map(instInfo, AppInstanceInfo.class);
        appInstanceInfo.setAppInstanceId(appInstanceId);

        appInstanceInfo = appInstanceInfoService.updateAppInstanceInfo(tenantId, appInstanceInfo);
        AppInstanceInfoDto dto = mapper.map(appInstanceInfo, AppInstanceInfoDto.class);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
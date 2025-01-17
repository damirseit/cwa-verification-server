/*-
 * ---license-start
 * Corona-Warn-App / cwa-verification
 * ---
 * Copyright (C) 2020 - 2022 T-Systems International GmbH and all other contributors
 * ---
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ---license-end
 */

package app.coronawarn.verification.covid_test_identifier_factory;

import app.coronawarn.verification.model.RegistrationTokenRequest;

public class COVIDTestIdentifierFactoryImpl implements COVIDTestIdentifierFactory{
  @Override
  public COVIDTestIdentifier makeCOVIDTestIdentifier(RegistrationTokenRequest request) {
    switch (request.getKeyType()) {
      case GUID:
        return new GUIDCOVIDTestIdentifier();
      case TELETAN:
        return new TeleTANCOVIDTestIdentifier();
      default:
        return new UnknownCOVIDTestIdentifier();
    }
  }
}

/*
 * Copyright 2019 ConsenSys AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package tech.pegasys.samples.crosschain.multichain.commands;

import java.util.Scanner;

/**
 * All options must implement this interface.
 *
 */
public interface MultichainManagerOptions {

  String getName();
  String getDescription();

  void interactive(final Scanner myInput) throws Exception;

  void help();

  void command(final String[] args, final int argOffset) throws Exception;
}
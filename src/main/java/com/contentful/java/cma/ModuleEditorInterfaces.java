/*
 * Copyright (C) 2017 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma;

import com.contentful.java.cma.RxExtensions.DefFunc;
import com.contentful.java.cma.model.CMAEditorInterface;

import java.util.concurrent.Executor;

import retrofit2.Retrofit;

/**
 * Editor Interfaces Module.
 */
public final class ModuleEditorInterfaces extends AbsModule<ServiceEditorInterfaces> {
  final Async async;

  /**
   * Create this module.
   *
   * @param retrofit         the retrofit instance to be used to create the service.
   * @param callbackExecutor to tell on which thread it should run.
   */
  public ModuleEditorInterfaces(Retrofit retrofit, Executor callbackExecutor) {
    super(retrofit, callbackExecutor);
    this.async = new Async();
  }

  @Override protected ServiceEditorInterfaces createService(Retrofit retrofit) {
    return retrofit.create(ServiceEditorInterfaces.class);
  }

  /**
   * @return the editor interface for a specific content type on a specific space.
   */
  public CMAEditorInterface fetchOne(String spaceId, String contentTypeId) {
    return service.fetchOne(spaceId, contentTypeId).toBlocking().first();
  }

  /**
   * Update an editor interface.
   *
   * @return the updated editor interface.
   */
  public CMAEditorInterface update(String spaceId,
                                   String contentTypeId,
                                   CMAEditorInterface editor) {
    final Integer version = editor.getVersion();
    return service.update(spaceId, contentTypeId, editor, version).toBlocking().first();
  }

  /**
   * @return a module with a set of asynchronous methods.
   */
  public Async async() {
    return async;
  }

  /**
   * Async module.
   */
  public final class Async {
    /**
     * Fetch editor interface to given content type in a given space.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAEditorInterface> fetchOne(
        final String spaceId,
        final String contentTypeId,
        CMACallback<CMAEditorInterface> callback) {
      return defer(new DefFunc<CMAEditorInterface>() {
        @Override CMAEditorInterface method() {
          return ModuleEditorInterfaces.this.fetchOne(spaceId, contentTypeId);
        }
      }, callback);
    }

    /**
     * Update the given editor interface.
     *
     * @return the callback to be informed about success or failure.
     */
    public CMACallback<CMAEditorInterface> update(
        final String spaceId,
        final String contentTypeId,
        final CMAEditorInterface editor,
        CMACallback<CMAEditorInterface> callback) {
      return defer(new DefFunc<CMAEditorInterface>() {
        @Override CMAEditorInterface method() {
          return ModuleEditorInterfaces.this.update(spaceId, contentTypeId, editor);
        }
      }, callback);
    }
  }
}
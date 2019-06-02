package eu.mobilebear.babylon.presentation.injection.scopes

import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * Identifies a type that the injector only instantiates once
 * per activity context.
 *
 * @see javax.inject.Scope @Scope
 */
@javax.inject.Scope
@MustBeDocumented
@Retention(RUNTIME)
annotation class ActivityScope

package com.serty.minirpg.injection;

import javax.inject.Singleton;

import com.serty.minirpg.MiniRpg;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component
public interface AppComponent {
    void inject(MiniRpg plugin);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder plugin(MiniRpg plugin);

        AppComponent build();
    }
}

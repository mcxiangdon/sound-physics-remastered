package com.sonicether.soundphysics.integration;

import com.sonicether.soundphysics.SoundPhysicsMod;
import com.sonicether.soundphysics.config.ReflectivityConfig;
import de.maxhenkel.configbuilder.ConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.SoundType;

import java.util.Map;

public class ClothConfigIntegration {

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder
                .create()
                .setParentScreen(parent)
                .setTitle(new TranslatableComponent("cloth_config.soundphysics.settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableComponent("cloth_config.soundphysics.category.general"));

        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.enabled"), SoundPhysicsMod.CONFIG.enabled));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.attenuation_factor"), SoundPhysicsMod.CONFIG.attenuationFactor));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.reverb_gain"), SoundPhysicsMod.CONFIG.reverbGain));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.reverb_brightness"), SoundPhysicsMod.CONFIG.reverbBrightness));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.block_absorption"), SoundPhysicsMod.CONFIG.blockAbsorption));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.block_reflectivity_factor"), SoundPhysicsMod.CONFIG.blockReflectivityFactor));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.default_block_reflectivity"), SoundPhysicsMod.CONFIG.defaultBlockReflectivity));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.sound_distance_allowance"), SoundPhysicsMod.CONFIG.soundDistanceAllowance));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.air_absorption"), SoundPhysicsMod.CONFIG.airAbsorption));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.underwater_filter"), SoundPhysicsMod.CONFIG.underwaterFilter));
        general.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.sound_blacklist_regex"), SoundPhysicsMod.CONFIG.soundBlacklistRegex));

        ConfigCategory performance = builder.getOrCreateCategory(new TranslatableComponent("cloth_config.soundphysics.category.performance"));

        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.environment_evaluation_ray_count"), SoundPhysicsMod.CONFIG.environmentEvaluationRayCount));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.environment_evaluation_ray_bounces"), SoundPhysicsMod.CONFIG.environmentEvaluationRayBounces));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.simpler_shared_airspace_simulation"), SoundPhysicsMod.CONFIG.simplerSharedAirspaceSimulation));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.non_full_block_occlusion_factor"), SoundPhysicsMod.CONFIG.nonFullBlockOcclusionFactor));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.max_occlusion"), SoundPhysicsMod.CONFIG.maxOcclusion));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.strict_occlusion"), SoundPhysicsMod.CONFIG.strictOcclusion));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.sound_direction_evaluation"), SoundPhysicsMod.CONFIG.soundDirectionEvaluation));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.redirect_non_occluded_sounds"), SoundPhysicsMod.CONFIG.redirectNonOccludedSounds));
        performance.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.max_direction_variance"), SoundPhysicsMod.CONFIG.maxDirectionVariance));


        ConfigCategory reflectivity = builder.getOrCreateCategory(new TranslatableComponent("cloth_config.soundphysics.category.reflectivity"));

        Map<SoundType, Double> defaultMap = SoundPhysicsMod.REFLECTIVITY_CONFIG.createDefaultMap();

        for (Map.Entry<SoundType, Double> entry : SoundPhysicsMod.REFLECTIVITY_CONFIG.getReflectivities().entrySet()) {
            DoubleListEntry e = entryBuilder
                    .startDoubleField(ReflectivityConfig.getNameComponent(entry.getKey()), entry.getValue())
                    .setMin(0.01F)
                    .setMax(10F)
                    .setDefaultValue(defaultMap.getOrDefault(entry.getKey(), SoundPhysicsMod.CONFIG.defaultBlockReflectivity.get()))
                    .setSaveConsumer(value -> SoundPhysicsMod.REFLECTIVITY_CONFIG.setReflectivity(entry.getKey(), value).save()).build();
            reflectivity.addEntry(e);
        }

        ConfigCategory logging = builder.getOrCreateCategory(new TranslatableComponent("cloth_config.soundphysics.category.logging"));

        logging.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.debug_logging"), SoundPhysicsMod.CONFIG.debugLogging));
        logging.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.occlusion_logging"), SoundPhysicsMod.CONFIG.occlusionLogging));
        logging.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.environment_logging"), SoundPhysicsMod.CONFIG.environmentLogging));
        logging.addEntry(fromConfigEntry(entryBuilder, new TranslatableComponent("cloth_config.soundphysics.performance_logging"), SoundPhysicsMod.CONFIG.performanceLogging));

        return builder.build();
    }

    private static <T> AbstractConfigListEntry<T> fromConfigEntry(ConfigEntryBuilder entryBuilder, Component name, ConfigEntry<T> entry) {
        if (entry instanceof de.maxhenkel.configbuilder.ConfigBuilder.DoubleConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startDoubleField(name, e.get())
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> {
                        e.set(d).save();
                        SoundPhysicsMod.CONFIG.reload();
                    })
                    .build();
        } else if (entry instanceof de.maxhenkel.configbuilder.ConfigBuilder.IntegerConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startIntField(name, e.get())
                    .setMin(e.getMin())
                    .setMax(e.getMax())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> {
                        e.set(d).save();
                        SoundPhysicsMod.CONFIG.reload();
                    })
                    .build();
        } else if (entry instanceof de.maxhenkel.configbuilder.ConfigBuilder.BooleanConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startBooleanToggle(name, e.get())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> {
                        e.set(d).save();
                        SoundPhysicsMod.CONFIG.reload();
                    })
                    .build();
        } else if (entry instanceof de.maxhenkel.configbuilder.ConfigBuilder.StringConfigEntry e) {
            return (AbstractConfigListEntry<T>) entryBuilder
                    .startStrField(name, e.get())
                    .setDefaultValue(e::getDefault)
                    .setSaveConsumer(d -> {
                        e.set(d).save();
                        SoundPhysicsMod.CONFIG.reload();
                    })
                    .build();
        }

        return null;
    }

}
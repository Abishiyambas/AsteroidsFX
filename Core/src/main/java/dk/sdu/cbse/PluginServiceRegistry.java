package dk.sdu.cbse;

import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginServiceRegistry {

    private static final String DEFAULT_PLUGINS_DIR = "plugins";

    private final ModuleLayer pluginLayer;

    public PluginServiceRegistry() {
        this.pluginLayer = loadPluginLayer();
    }

    public <T> List<T> loadServices(Class<T> serviceType) {
        List<T> services = new ArrayList<>();
        ServiceLoader.load(serviceType).forEach(services::add);
        if (pluginLayer != null) {
            ServiceLoader.load(pluginLayer, serviceType).forEach(services::add);
        }
        return services;
    }

    private ModuleLayer loadPluginLayer() {
        Path pluginsDir = Paths.get(System.getProperty("game.plugins.dir", DEFAULT_PLUGINS_DIR));
        if (!Files.isDirectory(pluginsDir)) {
            return null;
        }

        ModuleFinder finder = ModuleFinder.of(pluginsDir);
        Set<String> moduleNames = finder.findAll().stream()
                .map(reference -> reference.descriptor().name())
                .collect(Collectors.toSet());

        if (moduleNames.isEmpty()) {
            return null;
        }

        Configuration parentConfiguration = ModuleLayer.boot().configuration();
        Configuration configuration = parentConfiguration.resolve(finder, ModuleFinder.of(), moduleNames);
        return ModuleLayer.boot().defineModulesWithOneLoader(configuration, ClassLoader.getSystemClassLoader());
    }
}

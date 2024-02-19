package notepad.plugin;

import java.util.*;
import java.util.stream.Collectors;

public class PluginGetter {

    public PluginGetter() {

    }

    public static List<Plugin> getPlugins(){
        ServiceLoader<Plugin> serviceLoader = ServiceLoader.load(Plugin.class);
        List<Plugin> lista = serviceLoader.stream().map(a->a.get()).collect(Collectors.toList());
        return lista;
    }
}

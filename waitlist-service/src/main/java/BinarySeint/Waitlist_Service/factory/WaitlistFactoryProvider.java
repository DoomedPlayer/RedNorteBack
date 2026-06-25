package BinarySeint.Waitlist_Service.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaitlistFactoryProvider {

    private final Map<String, WaitlistFactoryMethod> factories;
    @Autowired
    public WaitlistFactoryProvider(List<WaitlistFactoryMethod> factoryList) {
        this.factories = factoryList.stream()
                .collect(Collectors.toMap(
                        factory -> factory.getTipoAtencion().toUpperCase(), 
                        Function.identity()
                ));
    }

    public WaitlistFactoryMethod obtenerFabrica(String tipoAtencion) {
        WaitlistFactoryMethod factory = factories.get(tipoAtencion.toUpperCase());
        if (factory == null) {
            return factories.get("CONSULTA"); 
        }
        return factory;
    }
}
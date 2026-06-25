package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.factory.impl.CirugiaFactory;
import BinarySeint.Waitlist_Service.factory.impl.ConsultaFactory;
import BinarySeint.Waitlist_Service.factory.impl.ProcedimientoFactory;
import BinarySeint.Waitlist_Service.factory.impl.UrgenciaFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WaitlistFactoryProviderTest {

    private WaitlistFactoryProvider factoryProvider;
    private CirugiaFactory cirugiaFactory;
    private ConsultaFactory consultaFactory;
    private ProcedimientoFactory procedimientoFactory;
    private UrgenciaFactory urgenciaFactory;

    @BeforeEach
    void setUp() {
        cirugiaFactory = new CirugiaFactory();
        consultaFactory = new ConsultaFactory();
        procedimientoFactory = new ProcedimientoFactory();
        urgenciaFactory = new UrgenciaFactory();

        factoryProvider = new WaitlistFactoryProvider(Arrays.asList(
                cirugiaFactory, 
                consultaFactory, 
                procedimientoFactory, 
                urgenciaFactory
        ));
    }

    @Test
    void testObtenerFabrica_CuandoExiste_RetornaFabricaCorrecta() {
        WaitlistFactoryMethod resultado = factoryProvider.obtenerFabrica("CIRUGIA");
        assertEquals(cirugiaFactory, resultado);
    }

    @Test
    void testObtenerFabrica_InsensibleAMayusculasYMinusculas() {

        WaitlistFactoryMethod resultado = factoryProvider.obtenerFabrica("uRgEnCiA");
        assertEquals(urgenciaFactory, resultado);
    }

    @Test
    void testObtenerFabrica_CuandoNoExiste_RetornaFallbackConsulta() {
        WaitlistFactoryMethod resultado = factoryProvider.obtenerFabrica("TIPO_INVENTADO_XYZ");
        assertEquals(consultaFactory, resultado);
    }
}
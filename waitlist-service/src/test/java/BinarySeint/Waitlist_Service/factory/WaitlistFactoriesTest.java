package BinarySeint.Waitlist_Service.factory;

import BinarySeint.Waitlist_Service.factory.impl.CirugiaFactory;
import BinarySeint.Waitlist_Service.factory.impl.ConsultaFactory;
import BinarySeint.Waitlist_Service.factory.impl.ProcedimientoFactory;
import BinarySeint.Waitlist_Service.factory.impl.UrgenciaFactory;
import BinarySeint.Waitlist_Service.model.ListaEspera;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaitlistFactoriesTest {

    @Test
    void testCirugiaFactory_CreaRegistroCorrectamente() {
        CirugiaFactory factory = new CirugiaFactory();
        
        assertEquals("CIRUGIA", factory.getTipoAtencion());

        ListaEspera registro = factory.crearRegistro("12345678-9", 10);
        assertNotNull(registro);
        assertEquals("12345678-9", registro.getRutPaciente());
        assertEquals(10, registro.getIdEspecialidad());
        assertEquals("Cirugía", registro.getTipoAtencion());
        assertEquals(2, registro.getNivelPrioridad());
        assertNotNull(registro.getFechaIngreso());
    }

    @Test
    void testConsultaFactory_CreaRegistroCorrectamente() {
        ConsultaFactory factory = new ConsultaFactory();
        
        assertEquals("CONSULTA", factory.getTipoAtencion());

        ListaEspera registro = factory.crearRegistro("11111111-1", 20);
        assertNotNull(registro);
        assertEquals("11111111-1", registro.getRutPaciente());
        assertEquals(20, registro.getIdEspecialidad());
        assertEquals("Consulta", registro.getTipoAtencion());
        assertEquals(5, registro.getNivelPrioridad());
        assertNotNull(registro.getFechaIngreso());
    }

    @Test
    void testProcedimientoFactory_CreaRegistroCorrectamente() {
        ProcedimientoFactory factory = new ProcedimientoFactory();
        
        assertEquals("PROCEDIMIENTO", factory.getTipoAtencion());

        ListaEspera registro = factory.crearRegistro("22222222-2", 30);
        assertNotNull(registro);
        assertEquals("22222222-2", registro.getRutPaciente());
        assertEquals(30, registro.getIdEspecialidad());
        assertEquals("Procedimiento", registro.getTipoAtencion());
        assertEquals(3, registro.getNivelPrioridad());
        assertNotNull(registro.getFechaIngreso());
    }

    @Test
    void testUrgenciaFactory_CreaRegistroCorrectamente() {
        UrgenciaFactory factory = new UrgenciaFactory();
        
        assertEquals("URGENCIA", factory.getTipoAtencion());

        ListaEspera registro = factory.crearRegistro("33333333-3", 40);
        assertNotNull(registro);
        assertEquals("33333333-3", registro.getRutPaciente());
        assertEquals(40, registro.getIdEspecialidad());
        assertEquals("Urgencia", registro.getTipoAtencion());
        assertEquals(1, registro.getNivelPrioridad());
        assertNotNull(registro.getFechaIngreso());
    }
}
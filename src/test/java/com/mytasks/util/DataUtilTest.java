package com.mytasks.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class DataUtilTest {

    @Test
    public void converteDataValida() {
        assertEquals(LocalDate.of(2026, 3, 20), DataUtil.converter("20/03/2026"));
    }

    @Test
    public void ignoraEspacosNasPontas() {
        assertEquals(LocalDate.of(2026, 3, 20), DataUtil.converter("  20/03/2026 "));
    }

    @Test
    public void aceitaDia29DeFevereiroEmAnoBissexto() {
        assertEquals(LocalDate.of(2028, 2, 29), DataUtil.converter("29/02/2028"));
    }

    @Test
    public void recusaDia29DeFevereiroEmAnoNaoBissexto() {
        assertNull(DataUtil.converter("29/02/2026"));
    }

    @Test
    public void recusaDataQueNaoExiste() {
        assertNull(DataUtil.converter("31/02/2026"));
    }

    @Test
    public void recusaMesInvalido() {
        assertNull(DataUtil.converter("10/13/2026"));
    }

    @Test
    public void recusaFormatoDiferente() {
        assertNull(DataUtil.converter("2026-03-20"));
    }

    @Test
    public void recusaTextoQualquer() {
        assertNull(DataUtil.converter("amanha"));
    }

    @Test
    public void recusaTextoVazio() {
        assertNull(DataUtil.converter(""));
    }

    @Test
    public void recusaNull() {
        assertNull(DataUtil.converter(null));
    }

    @Test
    public void formataDataNoPadraoBrasileiro() {
        assertEquals("05/01/2026", DataUtil.formatar(LocalDate.of(2026, 1, 5)));
    }

    @Test
    public void formatarNullRetornaNull() {
        assertNull(DataUtil.formatar(null));
    }

    @Test
    public void converterEFormatarVoltamAoMesmoTexto() {
        assertEquals("20/03/2026", DataUtil.formatar(DataUtil.converter("20/03/2026")));
    }
}

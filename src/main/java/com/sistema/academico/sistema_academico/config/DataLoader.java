package com.sistema.academico.sistema_academico.config;

import com.sistema.academico.sistema_academico.entity.Estudiante;
import com.sistema.academico.sistema_academico.entity.Materia;
import com.sistema.academico.sistema_academico.entity.MateriaId;
import com.sistema.academico.sistema_academico.service.EstudianteService;
import com.sistema.academico.sistema_academico.service.MateriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Puebla la base de datos al arrancar la aplicación, SOLO si aún no hay
 * suficientes registros (así no duplica datos en cada reinicio).
 * Requisito del laboratorio: mínimo 1000 estudiantes y 20 materias.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private static final int MIN_ESTUDIANTES = 1000;
    private static final int MIN_MATERIAS = 20;
    private static final int LOTE = 200;

    private final EstudianteService estudianteService;
    private final MateriaService materiaService;

    private final Faker faker = new Faker(new Locale("es"));
    private final Random random = new Random();

    private static final String[] CARRERAS = {
            "Ingeniería de Sistemas", "Ingeniería Industrial", "Ingeniería Electrónica",
            "Administración de Empresas", "Contaduría Pública", "Derecho",
            "Medicina", "Psicología", "Arquitectura", "Diseño Gráfico"
    };

    private static final String[] MATERIAS_NOMBRE = {
            "Cálculo I", "Cálculo II", "Álgebra Lineal", "Física I", "Física II",
            "Programación I", "Programación II", "Estructuras de Datos", "Bases de Datos",
            "Sistemas Distribuidos", "Redes de Computadores", "Ingeniería de Software",
            "Sistemas Operativos", "Arquitectura de Computadores", "Ética Profesional",
            "Investigación de Operaciones", "Estadística", "Economía", "Contabilidad General",
            "Inteligencia Artificial", "Machine Learning", "Cátedra Institucional",
            "Electiva Profesional I", "Electiva Profesional II"
    };

    @Override
    public void run(String... args) {
        poblarEstudiantes();
        poblarMaterias();
    }

    private void poblarEstudiantes() {
        long existentes = estudianteService.contar();
        if (existentes >= MIN_ESTUDIANTES) {
            log.info("Ya existen {} estudiantes, se omite la carga inicial", existentes);
            return;
        }

        long faltantes = MIN_ESTUDIANTES - existentes;
        log.info("Generando {} estudiantes sintéticos con Datafaker...", faltantes);

        List<Estudiante> lote = new ArrayList<>();
        for (long i = existentes; i < MIN_ESTUDIANTES; i++) {
            String codigo = String.format("EST%06d", i + 1);
            String nombre = faker.name().fullName();
            String correo = ("est" + (i + 1) + "." + faker.internet().username()).toLowerCase()
                    .replaceAll("[^a-z0-9.]", "") + "@universidad.edu.co";

            // Se calcula manualmente (en vez de faker.timeAndDate().birthday(...)) para no
            // depender del tipo de retorno de Datafaker, que cambió entre versiones.
            int edadAnios = 17 + random.nextInt(12); // entre 17 y 28 años
            LocalDate fechaNacimiento = LocalDate.now().minusYears(edadAnios).minusDays(random.nextInt(365));
            LocalDate fechaIngreso = LocalDate.now().minusMonths(random.nextInt(48));

            lote.add(Estudiante.builder()
                    .codigo(codigo)
                    .nombre(nombre)
                    .correo(correo)
                    .fechaIngreso(fechaIngreso)
                    .fechaNacimiento(fechaNacimiento)
                    .estado(random.nextInt(100) < 90) // ~90% activos
                    .carrera(CARRERAS[random.nextInt(CARRERAS.length)])
                    .build());

            if (lote.size() == LOTE) {
                estudianteService.guardarTodos(lote);
                lote.clear();
            }
        }
        if (!lote.isEmpty()) {
            estudianteService.guardarTodos(lote);
        }
        log.info("Carga de estudiantes completada.");
    }

    private void poblarMaterias() {
        long existentes = materiaService.contar();
        if (existentes >= MIN_MATERIAS) {
            log.info("Ya existen {} materias, se omite la carga inicial", existentes);
            return;
        }

        List<Materia> lote = new ArrayList<>();
        String[] grupos = {"A", "B", "C"};

        for (int i = 0; i < MATERIAS_NOMBRE.length && lote.size() < (MIN_MATERIAS - existentes); i++) {
            String codigo = String.format("MAT%03d", i + 1);
            String grupo = grupos[random.nextInt(grupos.length)];
            int creditos = 3 + random.nextInt(2); // 3 o 4

            lote.add(Materia.builder()
                    .id(new MateriaId(codigo, grupo))
                    .nombre(MATERIAS_NOMBRE[i])
                    .creditos(creditos)
                    .estado(true)
                    .build());
        }

        materiaService.guardarTodos(lote);
        log.info("Carga de {} materias completada.", lote.size());
    }
}

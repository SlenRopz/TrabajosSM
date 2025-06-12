package com.example.parcial;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {



    private RadioGroup radioGroupBicicletas;
    private CheckBox checkBoxCasco;
    private EditText editTextHoras;
    private Spinner spinnerPago;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchMoneda;
    private TextView textViewResultado;
    private Button buttonCalcular, buttonLimpiar;

    private final double MONTANA_PRECIO = 1000;
    private final double URBANA_PRECIO = 2000;
    private final double ELECTRICA_PRECIO = 4000;
    private final double TASA_DOLAR = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referencias a los elementos de la interfaz


        checkBoxCasco = findViewById(R.id.checkBoxCasco);
        editTextHoras = findViewById(R.id.editTextHoras);
        spinnerPago = findViewById(R.id.spinnerPago);
        switchMoneda = findViewById(R.id.switchMoneda);
        textViewResultado = findViewById(R.id.textViewResultado);
        buttonCalcular = findViewById(R.id.buttonCalcular);
        buttonLimpiar = findViewById(R.id.buttonLimpiar);

        // Mostrar Toast al seleccionar tipo de bicicleta
        radioGroupBicicletas.setOnCheckedChangeListener((group, checkedId) -> {
            String tipoBicicleta = "";
            if (checkedId == R.id.radioButtonMontana) {
                tipoBicicleta = "Montaña";
            } else if (checkedId == R.id.radioButtonUrbana) {
                tipoBicicleta = "Urbana";
            } else if (checkedId == R.id.radioButtonElectrica) {
                tipoBicicleta = "Eléctrica";
            }
            Toast.makeText(MainActivity.this, "Tipo de bicicleta: " + tipoBicicleta, Toast.LENGTH_SHORT).show();
        });

        // Botón Calcular
        buttonCalcular.setOnClickListener(v -> calcularImporte());

        // Botón Limpiar
        buttonLimpiar.setOnClickListener(v -> limpiarCampos());
    }

    private void calcularImporte() {
        // Obtener tipo de bicicleta
        double precioHora = 0;
        int selectedBicicletaId = radioGroupBicicletas.getCheckedRadioButtonId();
        if (selectedBicicletaId == R.id.radioButtonMontana) {
            precioHora = MONTANA_PRECIO;
        } else if (selectedBicicletaId == R.id.radioButtonUrbana) {
            precioHora = URBANA_PRECIO;
        } else if (selectedBicicletaId == R.id.radioButtonElectrica) {
            precioHora = ELECTRICA_PRECIO;
        }

        // Obtener número de horas
        int horas = Integer.parseInt(editTextHoras.getText().toString());

        // Calcular importe base
        double importe = precioHora * horas;

        // Aplicar casco adicional
        if (checkBoxCasco.isChecked()) {
            importe += importe * 0.20;
        }

        // Obtener tipo de pago
        String tipoPago = spinnerPago.getSelectedItem().toString();
        switch (tipoPago) {
            case "Efectivo":
                importe -= importe * 0.20;
                break;
            case "Transferencia":
                importe -= importe * 0.10;
                break;
            case "Tarjeta en cuotas":
                importe += importe * 0.10;
                break;
        }

        // Verificar si el resultado es en dólares
        if (switchMoneda.isChecked()) {
            importe /= TASA_DOLAR;
        }

        // Mostrar importe con dos decimales
        textViewResultado.setText(String.format("Importe: $%.2f", importe));
    }

    private void limpiarCampos() {
        radioGroupBicicletas.clearCheck();
        checkBoxCasco.setChecked(false);
        editTextHoras.setText("");
        spinnerPago.setSelection(0);
        switchMoneda.setChecked(false);
        textViewResultado.setText("Importe: $");
    }
}

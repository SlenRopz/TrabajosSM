public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "reservas.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE reservas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, dni TEXT, email TEXT, " +
                "habitacion TEXT, noches INTEGER, total REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS reservas");
        onCreate(db);
    }

    public void insertarReserva(String nombre, String dni, String email, String habitacion, int noches, double total) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("dni", dni);
        valores.put("email", email);
        valores.put("habitacion", habitacion);
        valores.put("noches", noches);
        valores.put("total", total);
        db.insert("reservas", null, valores);
    }

    public Cursor buscarPorDNI(String dni) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM reservas WHERE dni = ?", new String[]{dni});
    }
}


public class MainActivity extends AppCompatActivity {

    EditText edtNombre, edtDni, edtEmail, edtNoches;
    RadioGroup rgHabitaciones;
    TextView txtTotal;
    DBHelper dbHelper;
    String habitacionSeleccionada = "";
    double precioHabitacion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtNombre = findViewById(R.id.edtNombre);
        edtDni = findViewById(R.id.edtDni);
        edtEmail = findViewById(R.id.edtEmail);
        edtNoches = findViewById(R.id.edtNoches);
        rgHabitaciones = findViewById(R.id.rgHabitaciones);
        txtTotal = findViewById(R.id.txtTotal);

        dbHelper = new DBHelper(this);

        rgHabitaciones.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbEconomica:
                    habitacionSeleccionada = "Económica";
                    precioHabitacion = 1500;
                    break;
                case R.id.rbComoda:
                    habitacionSeleccionada = "Cómoda";
                    precioHabitacion = 3000;
                    break;
                case R.id.rbLujo:
                    habitacionSeleccionada = "De lujo";
                    precioHabitacion = 6000;
                    break;
            }
        });

        findViewById(R.id.btnCalcular).setOnClickListener(v -> calcularImporte());
        findViewById(R.id.btnCargar).setOnClickListener(v -> guardarReserva());
        findViewById(R.id.btnBuscar).setOnClickListener(v -> buscarReserva());
        findViewById(R.id.btnLimpiar).setOnClickListener(v -> limpiarCampos());
        findViewById(R.id.btnFactura).setOnClickListener(v -> verComprobante());
    }

    private void calcularImporte() {
        if (edtNoches.getText().toString().isEmpty() || precioHabitacion == 0) {
            Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            return;
        }
        int noches = Integer.parseInt(edtNoches.getText().toString());
        double total = precioHabitacion * noches;
        txtTotal.setText(String.format("$ %.2f", total));
    }

    private void guardarReserva() {
        String nombre = edtNombre.getText().toString();
        String dni = edtDni.getText().toString();
        String email = edtEmail.getText().toString();
        String nochesStr = edtNoches.getText().toString();

        if (nombre.isEmpty() || dni.isEmpty() || email.isEmpty() || nochesStr.isEmpty() || habitacionSeleccionada.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int noches = Integer.parseInt(nochesStr);
        double total = precioHabitacion * noches;
        dbHelper.insertarReserva(nombre, dni, email, habitacionSeleccionada, noches, total);
        Toast.makeText(this, "Reserva registrada", Toast.LENGTH_SHORT).show();
    }

    private void buscarReserva() {
        String dni = edtDni.getText().toString();
        if (dni.isEmpty()) {
            Toast.makeText(this, "Ingrese un DNI", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = dbHelper.buscarPorDNI(dni);
        if (cursor.moveToFirst()) {
            edtNombre.setText(cursor.getString(1));
            edtEmail.setText(cursor.getString(3));
            edtNoches.setText(cursor.getString(5));
            habitacionSeleccionada = cursor.getString(4);
            txtTotal.setText(String.format("$ %.2f", cursor.getDouble(6)));
        } else {
            Toast.makeText(this, "No encontrado", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void limpiarCampos() {
        edtNombre.setText("");
        edtDni.setText("");
        edtEmail.setText("");
        edtNoches.setText("");
        txtTotal.setText("$ 0,00");
        rgHabitaciones.clearCheck();
    }

    private void verComprobante() {
        Intent intent = new Intent(this, ComprobanteActivity.class);
        intent.putExtra("nombre", edtNombre.getText().toString());
        intent.putExtra("email", edtEmail.getText().toString());
        intent.putExtra("habitacion", habitacionSeleccionada);
        intent.putExtra("total", txtTotal.getText().toString());
        startActivity(intent);
    }
}


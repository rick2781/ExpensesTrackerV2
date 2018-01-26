package rick.expensestrackerv2.Presentation.MainActvity;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rick.expensestrackerv2.R;
import rick.expensestrackerv2.Utils.DividerItemDecoration;

//TODO ADD PROGRESS BAR on recyclerview
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MainActivityPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.addBill)
    FloatingActionButton addBill;

    @BindView(R.id.remainingFunds)
    TextView tvRemainingFunds;

    @BindView(R.id.grocerybutton)
    FloatingActionButton groceryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.ItemDecoration dividerDecoration = new DividerItemDecoration(
                ContextCompat.getDrawable(this, R.drawable.bg_rv_separator));

        presenter = new MainActivityPresenter(recyclerView, this, dividerDecoration);

        addBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.addNewBill(MainActivity.this);
            }
        });

        tvRemainingFunds.setText("Remaining Funds: $400");

        groceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.addNewGrocery(MainActivity.this, tvRemainingFunds);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.settings:
                presenter.newRemainingFundsLimit(this, tvRemainingFunds);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.saveBills(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveBills(this);
    }
}

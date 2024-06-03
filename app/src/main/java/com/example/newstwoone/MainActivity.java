package com.example.newstwoone;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newstwoone.Adapter.CategoryAdapter;
import com.example.newstwoone.Adapter.NewsAdapter;
import com.example.newstwoone.model.ArticleModel;
import com.example.newstwoone.model.CategoryModel;
import com.example.newstwoone.model.NewsModel;
import com.example.newstwoone.retrofit.RetrofitNewsAPI;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickInterface {

    private ProgressBar loadingPB;
    private ArrayList<ArticleModel> articlesArrayList;
    private ArrayList<CategoryModel> categoryRVModalArrayList;
    private CategoryAdapter categoryRVAdapter;
    private NewsAdapter newsAdapter;
    private SearchView searchView;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsRV = findViewById(R.id.rvNews);
        RecyclerView categoryRV = findViewById(R.id.rvCategories);
        loadingPB = findViewById(R.id.pbLoading);
        searchView = findViewById(R.id.searchView);

        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();

        newsAdapter = new NewsAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryAdapter(categoryRVModalArrayList, this, this);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsAdapter);
        categoryRV.setAdapter(categoryRVAdapter);

        getCategories();
        getNews("Top Headlines");
        newsAdapter.notifyDataSetChanged();

        // Setup SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNews(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterNews(newText);
                return false;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getCategories() {
        categoryRVModalArrayList.add(new CategoryModel("Top Headlines", "https://images.unsplash.com/photo-1552764217-6d34d9795ab9?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fHdvcmxkfGVufDB8fDB8fHww"));
        categoryRVModalArrayList.add(new CategoryModel("Technology", "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8VGVjaG5vbG9neXxlbnwwfHwwfHx8MA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryModel("Science", "https://images.unsplash.com/photo-1628595351029-c2bf17511435?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTV8fHNjaWVuY2V8ZW58MHx8MHx8fDA%3D"));
        categoryRVModalArrayList.add(new CategoryModel("Sports", "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8c3BvcnRzfGVufDB8fDB8fHww"));
        categoryRVModalArrayList.add(new CategoryModel("General", "https://media.istockphoto.com/id/524426975/photo/new-york-state-capitol-building-albany.webp?b=1&s=170667a&w=0&k=20&c=0tFCLRnNE5FuNgIsX37f5OkRhe57iVkXh0mmogYG5wA="));
        categoryRVModalArrayList.add(new CategoryModel("Business", "https://media.istockphoto.com/id/1474838897/photo/business-partner-handshake-after-the-meeting-financial-and-investment-cooperation.webp?b=1&s=170667a&w=0&k=20&c=UfrKkB2oU0j0VwYPcmRJIwwg32LYMZcezMQdYJf4j0E="));
        categoryRVModalArrayList.add(new CategoryModel("Entertainment", "https://images.unsplash.com/photo-1499364615650-ec38552f4f34?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHx8MA%3D%3D"));
        categoryRVModalArrayList.add(new CategoryModel("Health", "https://plus.unsplash.com/premium_photo-1672760403439-bf51a26c1ae6?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OXx8aGVhbHRoY2FyZXxlbnwwfHwwfHx8MA%3D%3D"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category) {
        Log.d("checkNew - marcel", "getNews method called with category: " + category);

        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines/?country=us&category=" + category + "&apiKey=dd65f4d5ebd343d6ac81d1b35ce049c1";
        String url = "https://newsapi.org/v2/top-headlines/?country=us&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=dd65f4d5ebd343d6ac81d1b35ce049c1";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitNewsAPI retrofitAPI = retrofit.create(RetrofitNewsAPI.class);
        Call<NewsModel> call;
        if (category.equals("Top Headlines")) {
            call = retrofitAPI.getAllNews(url);
        } else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<NewsModel> call, @NonNull Response<NewsModel> response) {
                Log.d("checkNew - marcel", "API call successful, response code: " + response.code());

                if (response.body() != null) {
                    NewsModel newsModal = response.body();
                    ArrayList<ArticleModel> articles = newsModal.getArticles();
                    Log.d("checkNew - marcel", "Number of articles received: " + articles.size());

                    for (ArticleModel article : articles) {
                        articlesArrayList.add(new ArticleModel(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrlToImage(),
                                article.getUrl(),
                                article.getContent()));
                    }

                    newsAdapter.notifyDataSetChanged();
                } else {
                    Log.d("checkNew - marcel", "Response body is null");
                }

                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<NewsModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterNews(String query) {
        ArrayList<ArticleModel> filteredList = new ArrayList<>();
        for (ArticleModel item : articlesArrayList) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        newsAdapter.updateNewsList(filteredList);
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}

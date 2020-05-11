package com.github.franckyi.emerald.service.web;

import com.github.franckyi.emerald.util.IBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CallHandler<T> {
    private final Call<T> call;
    private OnResponseHandler<T> onResponse;
    private OnFailureHandler<T> onFailure;

    private CallHandler(Call<T> call) {
        this.call = call;
    }

    public static <T> Builder<T> builder(Call<T> call) {
        return new Builder<>(call);
    }

    public Call<T> getCall() {
        return call;
    }

    public OnResponseHandler<T> getOnResponse() {
        return onResponse;
    }

    public void setOnResponse(OnResponseHandler<T> onResponse) {
        this.onResponse = onResponse;
    }

    public OnFailureHandler<T> getOnFailure() {
        return onFailure;
    }

    public void setOnFailure(OnFailureHandler<T> onFailure) {
        this.onFailure = onFailure;
    }

    public T run() {
        try {
            Response<T> response = call.execute();
            this.onResponse(call, response);
            return response.body();
        } catch (Throwable t) {
            this.onFailure(call, t);
            return null;
        }
    }

    public void runAsync() {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                CallHandler.this.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                CallHandler.this.onFailure(call, t);
            }
        });
    }

    private void onResponse(Call<T> call, Response<T> response) {
        if (onResponse != null) {
            onResponse.accept(call, response);
        }
    }

    private void onFailure(Call<T> call, Throwable t) {
        if (onFailure != null) {
            onFailure.accept(call, t);
        }
    }

    @FunctionalInterface
    public interface OnResponseHandler<T> extends BiConsumer<Call<T>, Response<T>> {
    }

    @FunctionalInterface
    public interface OnFailureHandler<T> extends BiConsumer<Call<T>, Throwable> {
    }

    public static class Builder<T> implements IBuilder<CallHandler<T>> {
        private final CallHandler<T> handler;

        public Builder(Call<T> call) {
            this.handler = new CallHandler<>(call);
        }

        public Builder<T> onResponse(OnResponseHandler<T> onResponse) {
            handler.setOnResponse(onResponse);
            return this;
        }

        public Builder<T> onResponse(Consumer<T> onResponse) {
            return this.onResponse((call, response) -> onResponse.accept(response.body()));
        }

        public Builder<T> onFailure(OnFailureHandler<T> onFailure) {
            handler.setOnFailure(onFailure);
            return this;
        }

        public Builder<T> onFailure(Consumer<Throwable> onFailure) {
            return this.onFailure((call, t) -> onFailure.accept(t));
        }

        @Override
        public CallHandler<T> build() {
            return handler;
        }
    }

}

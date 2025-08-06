# Step 1: Build Angular App
FROM node:18 as build
WORKDIR /app

# Copy package files first for better caching
COPY package*.json ./
RUN npm ci

# Copy source code
COPY . .

# Build the application
RUN npm run build -- --configuration production

# Step 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/frontend /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

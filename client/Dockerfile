# STAGE 1: Build Angular App
FROM node:20 as build
WORKDIR /app
COPY package.json ./
RUN npm install
COPY . . 
RUN npm run build

# STAGE 2: Nginx to server the app
FROM nginx:stable-alpine
COPY --from=build /app/dist/client/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
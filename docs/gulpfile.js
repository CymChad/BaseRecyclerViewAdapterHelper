// import gulp from 'gulp';
// import sass from 'gulp-sass';
// import imagemin from 'gulp-imagemin';
// import autoprefixer from 'gulp-autoprefixer';
// import notify from 'gulp-notify';
// import livereload from 'gulp-livereload';
// import del from 'del';
var gulp = require('gulp'),
  sass = require('gulp-sass'),
  imagemin = require('gulp-imagemin'),
  autoprefixer = require('gulp-autoprefixer'),
  connect = require('gulp-connect'),
  uglify = require('gulp-uglify'),
  del = require('del'),
  pngquant = require('imagemin-pngquant'),
  cache = require('gulp-cache');


gulp.task('styles', function () {
  return gulp.src('src/style.scss')
    .pipe(sass({ style: 'expanded'}))
    .pipe(autoprefixer('last 2 version', 'ie 9'))
    .pipe(gulp.dest('./'));
});

gulp.task('images', function () {
  return gulp.src('src/img/**/*')
    .pipe(cache(imagemin([
      pngquant(),
    ]))).pipe(gulp.dest('./img'));
});

gulp.task('scripts', function () {
  return gulp.src('src/index.js')
    .pipe(gulp.dest('./'));
});

gulp.task('html', function () {
  return gulp.src('src/index.html')
    .pipe(gulp.dest('./'));
});

gulp.task('clean', function () {
  del(['img/', 'index.js', 'index.html', 'style.css'])
});

gulp.task('default', ['clean'], function () {
  gulp.start('styles', 'images', 'scripts', 'html')
});

gulp.task('watch', function () {
  //gulp.watch('src/style.scss', ['styles']);
  gulp.watch('src/index.js', ['scripts']);
  gulp.watch('src/index.html', ['html']);
  gulp.watch('src/img/**/*', ['images']);

  gulp.watch(['index.html', 'index.js', 'style.css', 'img/**'])
    .on('change', connect.reload);
  connect.server({
    livereload: true
  });
});


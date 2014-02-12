module.exports = function (grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),

		/**
		 * CONCAT
		 * Concatenation of :
		 * - app_js: all project files
		 * - plugins_js: all plugins without Angular and jQuery
		 * - plugins_css: all plugins stylesheets
		 */
		concat: {
			options: {
				separator: ';'
			},
			app_js: {
				src: ['app/js/**/*.js'],
				dest: 'dist/tmp/<%= pkg.name %>.js'
			},
			plugins_js: {
				src: ['app/lib/plugins/**/*.js'],
				dest: 'dist/tmp/plugins.js'
			},
		},
		/**
		 * UGLIFY
		 * Minimization of concatenated js files
		 */
		uglify: {
			options: {
				// the banner is inserted at the top of the output
				banner: '/*! <%= pkg.name %> <%= pkg.version %> [<%= grunt.template.today("dd-mm-yyyy") %>] */\n',
				mangle: false
			},
			dist: {
				files: {
					'dist/js/<%= pkg.name %>.min.js': ['<%= concat.app_js.dest %>'],
					'dist/lib/plugins.min.js': ['<%= concat.plugins_js.dest %>']
				}
			}
		},
		/**
		 * COPY
		 */
		copy: {
			production: {
				files: [
					{cwd: 'app/', src: 'index.html', expand: true, dest: 'dist/', ext: '.html'},
					{cwd: 'app', src: 'img/**', expand: true, dest: 'dist/'},
					{cwd: 'app', src: 'components/*', expand: true, dest: 'dist/'},
					{cwd: 'app', src: 'partials/**', expand: true, dest: 'dist/'},
					{cwd: 'app', src: 'lib/**', expand: true, dest: 'dist/'},
					{cwd: 'app', src: 'css/**', expand: true, dest: 'dist/'}
				]
			}
		},
		/**
		 * LESS
		 * Compilation of app.less to CSS
		 */
		less: {
			production: {
				options: {
					paths: ["assets/css"],
					yuicompress: true
				},
				files: {
					"dist/css/<%= pkg.name %>.min.css": "app/css/app.less"
				}
			}
		},
		/**
		 * JSHINT
		 * Validate js files
		 */
		jshint: {
			// define the files to lint
			files: ['app/js/**/*.js'],
			// configure JSHint (documented at http://www.jshint.com/docs/)
			options: {
				// more options here if you want to override JSHint defaults
				globals: {
					console: true,
					module: true,
					smarttabs: false,
					angular: false
				}
			}
		},
		/**
		 * HASHRES
		 * Rename js and css files to invalidate the browsers cache
		 */
		hashres: {
			production: {
				src: ['dist/js/<%= pkg.name %>.min.js',
					'dist/lib/plugins.min.js',
					'dist/css/<%= pkg.name %>.min.css'],
				dest: 'dist/index.html'
			}
		},
		/**
		 * CLEAN
		 * Remove the tmp directory
		 */
		clean: {
			temp: {
				src: ["dist/tmp"]
			},
			all: {
				src: ["dist/**"]
			}
		}
	});

	/**
	 * Load npm tasks modules
	 */
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-less');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-hashres');
	grunt.loadNpmTasks('grunt-contrib-clean');

	/**
	 * Register batch tasks:
	 */
	grunt.registerTask('default', ['clean:all', 'concat', 'uglify', 'less', 'copy', 'hashres', 'clean:temp']);
};
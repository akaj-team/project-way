# github comment settings
github.dismiss_out_of_range_messages

# Warn when there are merge commits in the diff
if git.commits.any? { |c| c.message =~ /^Merge branch 'rxanko'/ }
    warn 'Please rebase to get rid of the merge commits in this PR'
end

checkstyle_format.base_path = Dir.pwd

# detekt
checkstyle_format.report 'app/build/reports/detekt/detekt-checkstyle.xml'

# # Android Lint
require 'android_lint_translate_checkstyle_format'
android_lint_xml = ::AndroidLintTranslateCheckstyleFormat::Script.translate(File.read('app/build/reports/lint-results.xml'))
checkstyle_format.report_by_text android_lint_xml